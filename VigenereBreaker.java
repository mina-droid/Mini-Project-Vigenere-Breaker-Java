import java.util.*;
import edu.duke.*;
import java.io.File;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder sb = new StringBuilder(message);
        String ans = "";
        for ( int i = whichSlice; i < message.length(); i += totalSlices)
        {
            char c = sb.charAt(i);
            ans += c;
        }
        //REPLACE WITH YOUR CODE
        return ans;
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        for ( int i = 0; i < klength; ++i)
        {
            String enc = sliceString(encrypted,i,klength);
            CaesarCracker obj = new CaesarCracker();
            key[i] = obj.getKey(enc);
        }
        
        //WRITE YOUR CODE HERE
        return key;
    }
    
    public HashSet <String> readDictionary(FileResource fr)
    {
        HashSet <String> hs = new HashSet<String>();
        for ( String word : fr.lines())
        {
            hs.add(word.toLowerCase());
        }
        return hs;
    }
    
    public int countWords(String message, HashSet <String> dictionary)
    {
        String [] words = message.split("\\W+");
        int wordsCount = 0;
        for(String word : words)
        {
            if (dictionary.contains(word.toLowerCase()))
            {
                wordsCount++;
            }
        }
        return wordsCount;
    }
    
    public String breakForLanguage(String encrypted, HashSet <String> dictionary)
    {
        int maxWords = 0;
        String ans = "";
        int keyLength = 0;
        int [] keyUsed = {0};
        char c = mostCommonCharIn(dictionary);
        for (int i = 1; i <= 100; ++i)
        {
            int [] key = tryKeyLength(encrypted,i,c);
            VigenereCipher vc = new VigenereCipher(key);
            String dec = vc.decrypt(encrypted);
            int wordsCount = countWords(dec, dictionary);
            if (wordsCount > maxWords)
            {
                maxWords = wordsCount;
                ans = dec;
                keyUsed = key;
                keyLength = i;
            }
        }
        System.out.println("most valid words = " + maxWords);
        for (int j = 0; j < keyLength; j++)
        {
            System.out.println(keyUsed[j]);
        }
        return ans;
    }
    
    public char mostCommonCharIn(HashSet <String> dictionary)
    {
        HashMap<Character,Integer>  charMap = new HashMap<Character,Integer>();
        int max = 0;
        char maxChar = 'a';
        for ( String word : dictionary)
        {
            for (int i = 0 ; i < word.length(); ++i)
            {
                char c = Character.toLowerCase(word.charAt(i));
                if (charMap.containsKey(c))
                {
                    charMap.put(c,charMap.get(c) + 1);
                }
                else
                {
                    charMap.put(c,1);
                }
            }
        }
        for ( char c : charMap.keySet())
        {
            int count = charMap.get(c);
            if (count > max)
            {
                max = count;
                maxChar = c;
            }
        }
        return maxChar;
    }
    
    public void  breakForAllLangs(String encrypted,HashMap<String,HashSet<String>> languages)
    {
        int max = 0;
        String langUsed = "";
        String decUsed = "";
        for ( String lang : languages.keySet())
        {
            String dec = breakForLanguage(encrypted, languages.get(lang));
            int count = countWords(dec,languages.get(lang));
            if ( count > max)
            {
                max = count;
                langUsed = lang;
                decUsed = dec;
            }
        }
        System.out.println(max);
        System.out.println(langUsed);
        System.out.println(decUsed);
    }
    

    public void breakVigenere () 
    {
        HashMap<String, HashSet<String>> languages = new HashMap<String, HashSet<String>>();
        FileResource fr1 = new FileResource();
        String encrypted = fr1.asString();
        DirectoryResource dr = new DirectoryResource();
        for ( File f : dr.selectedFiles())
        {
            String lang = f.getName();
            FileResource fr2 = new FileResource(f);
            HashSet hs =  readDictionary(fr2);
            if (!languages.containsKey(lang))
            {
              languages.put(lang,hs);
            }
        }
        breakForAllLangs(encrypted,languages);
        
        /*int [] key = tryKeyLength(fr1.asString(),38, 'e');
        VigenereCipher vc = new VigenereCipher(key);
        String ans = vc.decrypt(encrypted);
        int max = countWords(ans,hs);
        System.out.println(max);*/
        //WRITE YOUR CODE HERE
    }
    
    public void test()
    {
        FileResource fr = new FileResource();
        int [] key = tryKeyLength(fr.asString(),4, 'e');
        for ( int i = 0; i < 4; ++i)
        {
            System.out.println(key[i]);
        }
        
        
    }
    
}
