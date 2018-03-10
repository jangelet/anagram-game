/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.nfc.Tag;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();


    public AnagramDictionary(Reader reader) throws IOException {

        BufferedReader in = new BufferedReader(reader);

        String line;

        while((line = in.readLine()) != null) {

            String word = line.trim();
            int wordSize = word.length();
            wordSet.add(word);
            wordList.add(word);

            //sorts string alphabetically, ascending order
            String sort = sortLetters(word);

            //if lettersToWord has sorted string of letters as key, add unsorted word to it
            if(lettersToWord.containsKey(sort)){
                (lettersToWord.get(sort)).add(word);
            }else{
            //if lettersToWord doesn't have sorted string as key, create new array list of words
            //add current sorted key and unsorted word to that arraylist
            // (keeps track of every word that can be spelled)
                ArrayList<String> words = new ArrayList<String>();
                words.add(word);
                lettersToWord.put(sort, words);
            }

            //populates hash map that maps word length to array list of every word of that length
            if(sizeToWords.containsKey(wordSize)){
                (sizeToWords.get(wordSize)).add(word);
            }else{
                ArrayList<String> words2 = new ArrayList<String>();
                words2.add(word);
                sizeToWords.put(wordSize, words2);
            }
//            Log.d("mainActivity", "starterWords" + sizeToWords);
        }
    }

    public boolean isGoodWord(String word, String base) {

        //checks if wordSet has the hord and if the word DOES NOT contain base as a substring
        if(wordSet.contains(word) && !(word.contains(base))) return true;

        return false;
    }

    public String sortLetters(String word){

        //Returns string word sorted in alphabetical order
        char[] charArr = word.toCharArray();
        Arrays.sort(charArr);
        String result = Arrays.toString(charArr);
        return result;
    }

    public List<String> getAnagrams(String targetWord) {
//      for loop will check if word in wordlist is an anagram of targetWord
//        for(String w : wordSet)
//            if(w.length() == targetWord.length())
//                if(sortLetters(w).equals(sortLetters(targetWord)))
//                    result.add(w);

        ArrayList<String> result = new ArrayList<>();
        if(lettersToWord.containsKey(sortLetters(targetWord)))
            result.addAll(lettersToWord.get(sortLetters(targetWord)));

        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for(int i = 0; i < alphabet.length; i++){
            String wordPlusOne = word + String.valueOf(alphabet[i]);
            ArrayList<String> anagrams = (ArrayList<String>) getAnagrams(wordPlusOne);
            for (String w : anagrams)
                if(isGoodWord(w, word))
                    result.add(w);
        }
        return result;
    }

    public String pickGoodStarterWord() {
        Random r = new Random();
//      int wordLength = DEFAULT_WORD_LENGTH;

//        get random word of word of wordlength from word size hash map

        ArrayList<String> starterWords = sizeToWords.get(wordLength);

//        Log.d("pickgud", "starterWords" + starterWords);

        for(int i = r.nextInt(starterWords.size()); i < starterWords.size(); i++){
            String word = starterWords.get(i);
            word = sortLetters(word);
            if(lettersToWord.get(word).size() >= MIN_NUM_ANAGRAMS) {
                wordLength++;
                return starterWords.get(i);
            }
            if(i == starterWords.size() - 1){
                i = r.nextInt(starterWords.size()) - 1; //subtracting 1 in case new random int == wordList.size() - 1
            }
        }
        return "stop";
    }
}
