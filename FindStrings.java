import java.io.*;
import java.util.*;

/**
 * You are given n strings w1, w2, ......, wn.
 * Let Si denote the set of strings formed by considering all unique substrings of the string wi.
 * A substring is defined as a contiguous sequence of one or more characters in the string.
 * More information on substrings can be found here. Let S = {S1 U S2 U .... Sn} .i.e S is a set of strings formed by
 * considering all the unique strings in all sets S1, S2, ..... Sn.
 * You will be given many queries, and for each query, you will be given an integer 'k'.
 * Your task is to display the lexicographically kth smallest string from the set S.
 *
 * Input:
 *
 * The first line of input contains a single integer n, denoting the number of strings.
 * Each of the next n lines consists of a string. The string on the ith line (1<=i<=n) is denoted by wi and has a length mi.
 * The next line consists of a single integer q, denoting the number of queries.
 * Each of the next q lines consists of a single integer k.
 *
 * Note: The input strings consist only of lowercase english alphabets 'a' - 'z'.
 *
 * Output:
 *
 * Output q lines, where the ith line consists of a string which is the answer to the ith query.
 * If the input is invalid (i.e., k > size of S), display "INVALID" for that case.
 *
 * Constraints:
 *
 * 1<= n <=50 
 * 1<= mi <=2000 
 * 1<= q <=500 
 * 1<= k <=1000000000
 *
 * Sample Input: 
 * 2 
 * aab 
 * aac 
 * 3 
 * 3 
 * 8 
 * 23
 *
 * Sample Output: 
 * aab 
 * c 
 * INVALID
 *
 * Explanation:
 *
 * For the sample test case, we have 2 strings "aab" and "aac". 
 * S1 = {"a", "aa", "aab", "ab", "b"} . These are the 5 unique substrings of "aab". 
 * S2 = {"a", "aa", "aac",  "ac", "c" } . These are the 5 unique substrings of "aac". 
 * Now, S = {S1 U S2} = {"a", "aa", "aab", "aac", "ab", "ac", "b", "c"}. Totally, 8 unique strings are present in the set S. 
 * The lexicographically 3rd smallest string in S is "aab" and the lexicographically 8th smallest string in S is "c".
 * Since there are only 8 distinct substrings, the answer to the last query is "INVALID".
*/
public class FindStrings {
  public static void main(String[] args) throws java.lang.Exception {
    BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
	  int numStrings = Integer.parseInt(bi.readLine());
	  Set<String> suffixes = new HashSet<>();
	  for (int i=0; i<numStrings; i++) {
		  String input = bi.readLine();
		  for (int j=0; j<input.length(); j++) {
			  suffixes.add(input.substring(j));
		  }
	  }
	  ArrayList<String> sortedSuffixes = new ArrayList<>();
	  sortedSuffixes.addAll(suffixes);
	  sort(sortedSuffixes);
	  ArrayList<Integer> commonPrefix = new ArrayList<>();
	  commonPrefix.add(0);
	  ArrayList<Long> totalSubStrings = new ArrayList<>();
    totalSubStrings.add(0l);
	  totalSubStrings.add((long) sortedSuffixes.get(0).length());
	  for (int i=1; i<sortedSuffixes.size(); i++) {
		  int lengthPrefix = 0;
		  String prevString = sortedSuffixes.get(i-1);
		  String currentString = sortedSuffixes.get(i);
		  for (int j=0; j<prevString.length() && prevString.charAt(j) == currentString.charAt(j); j++) {
			  lengthPrefix++;
		  }
		  commonPrefix.add(lengthPrefix);
			totalSubStrings.add(totalSubStrings.get(i) + sortedSuffixes.get(i).length() - commonPrefix.get(i));
		}
		int numOutputs = Integer.parseInt(bi.readLine());
		for (int i=0; i<numOutputs; i++) {
		  long index = Long.parseLong(bi.readLine());
		  if (index > totalSubStrings.get(sortedSuffixes.size())) {
			  System.out.println("INVALID");
		  } else {
			  int j = 0;
			  while (totalSubStrings.get(j) < index) {
				  j++;
			  }
			  System.out.println(sortedSuffixes.get(j-1).substring(0, commonPrefix.get(j-1) + (int)(index - totalSubStrings.get(j-1))));
		  }
	  }
  }
	private static <T extends Comparable<? super T>> void sort(List<T> list) {
  	quickSort(list, 0, list.size() - 1);
	}
	private static <T extends Comparable<? super T>> void quickSort(List<T> array, int left, int right){
    if(left  >= right) {
    	return;
    }
    int q = partition(array, left, right);
    quickSort(array, left, q - 1);
    quickSort(array, q + 1, right);
	}
	private static <T extends Comparable<? super T>> int partition(List<T> array, int first, int last) {
    T key = array.get(last);
  	int smaller = first - 1;
  	for (int test = first; test < last; test++) {
      if (array.get(test).compareTo(key) <= 0) {
        smaller++;
        exchange(smaller, test, array);
      }
  	}
  	smaller++;
  	exchange(last, smaller, array);
  	return smaller;
	}
	private static <T extends Comparable<? super T>> void exchange(int j, int i, List<T> array) {
    	T temp = array.get(j);
    	array.set(j, array.get(i));
    	array.set(i, temp);
	}
}
