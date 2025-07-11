/**
 * Interface SortAndSearch
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package RedeFerroviaria;


/**
 * Interface SortAndSearch responsible for handling calculus, sorting and searching
 */
public interface SortAndSearch {
    int GROWTH_FACTOR = 2;

    static int binarySearch(String[] a, String s, int size){
        int left = 0;
        int right = size - 1;
        if (size == 0){
            return -1;
        }

        while (left <= right) {
            int mid = (left + right) / 2;
            int result = s.compareToIgnoreCase(a[mid]);

            if (result == 0)
                return mid;

            if (result > 0)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return -1;
    }

    static void sort(String[] a, int size){
        for (int i = 0; i < size - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < size; j++) {
                if (a[j].compareTo(a[min_idx]) < 0) {
                    min_idx = j;
                }
            }
            String temp = a[i];
            a[i] = a[min_idx];
            a[min_idx] = temp;
        }
    }

    static void insertSorted(String[] a, int size, int idx, String s){
        if (idx >= size){
            a[size] = s;
        } else {
            for(int i = size - 1; i >= idx; i--){
                a[i+1] = a[i];
            }
            a[idx] = s;
        }
    }

    static String[] grow(String[] a){
        String[] aux = new String[a.length * GROWTH_FACTOR];
        for (int i = 0; i < a.length; i++){
            aux[i] = a[i];
        }
        a = aux;
        return a;
    }
}


/**
 * End of SortAndSearch Interface
 */
