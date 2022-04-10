package busoute;


/*
    Searching for all trips with a given arrival time, returning full details of all trips matching the criteria (zero, one or more), 
    sorted by trip id
*/

/*
Searching for a bus stop by full name or by the first few characters in the name, 
using a ternary search tree (TST), returning the full stop information for each stop
matching the search criteria (which can be zero, one or more stops).
*/

import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

import java.io.FileNotFoundException;

public class tripFinder {
    private Boolean error = false;
    public int remainder;

    /**
     * returns all bus data associated with a given arrival time in increasing order
     * of trip_id
     **/

    public Boolean getErrorStatus() {
        return this.error;
    }

    // check how many comma's are present.
    private String addCommas(String data) {
        int count = 0;
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == ',') {
                count++;
            }
            remainder = 9 - count;
        }
        for (int i = 0; i < remainder; i++) {
            data += ",";
        }
        return data;
    }

    // if the time input contain prohibited values then return true
    private boolean prohibitedInput(String time) {
        try {
            String hours = time.split(":")[0];
            String mins = time.split(":")[1];
            String seconds = time.split(":")[2];

            if (!(hours.matches("\\d+"))) {
                return true;
            }

            if (!(mins.matches("\\d+"))) {
                return true;
            }

            if (!(seconds.matches("\\d+"))) {
                return true;
            }
            if (mins.toCharArray().length != 2) {
                return true;
            }
            if (seconds.toCharArray().length != 2) {
                return true;

            }
        } catch (Exception e) {
            return true;
        }
        return false;

    }

    public String[] getStopByArival(String time) {
        if (!(prohibitedInput(time))) {
            boolean add_to_list = false;
            this.error = true;
            // create an arraylist to hold the data
            ArrayList<String> s = new ArrayList<String>();
            String[] array;

            String fileName = "inputs\\stop_times.txt";
            File file = new File(fileName);
            System.out.println(" ");
            try {
                Scanner inputStream = new Scanner(file);
                while (inputStream.hasNextLine()) {
                    String data = inputStream.nextLine();
                    // data = addCommas(data);

                    String[] d = data.split(",");
                    // System.out.println("the val of ->" + d[0]);
                    if (d[0].compareTo("trip_id") != 0) {

                        // split the arrival time
                        String[] arrival_times = d[1].split(":");

                        // split the hours into unit and seconds
                        String[] hours = arrival_times[0].split(" ");
                        if (hours[0].compareTo("") == 0 || hours[0].compareTo("0") == 0
                                || hours[0].compareTo("0") == 0) {
                            int t = Integer.parseInt(time.split(":")[0].split(" ")[0]);
                            if (Integer.parseInt(hours[1]) < 0 || Integer.parseInt(hours[1]) != t
                                    || Integer.parseInt(time.split(":")[1]) != Integer.parseInt(arrival_times[1])
                                    || Integer.parseInt(time.split(":")[2]) != Integer.parseInt(arrival_times[2])) {
                                add_to_list = false;
                            }
                        } else {
                            // if the hours are invalid then we won't add the data to the list
                            if (Integer.parseInt(arrival_times[0]) > 23 || Integer.parseInt(arrival_times[0]) < 0
                                    || Integer.parseInt(arrival_times[0]) != Integer.parseInt(time.split(":")[0])) {
                                add_to_list = false;
                            }
                        }
                        // if invalid mins or seconds.
                        if (Integer.parseInt(arrival_times[1]) > 59 || Integer.parseInt(arrival_times[2]) > 59
                                || Integer.parseInt(time.split(":")[1]) != Integer.parseInt(arrival_times[1])
                                || Integer.parseInt(time.split(":")[2]) != Integer.parseInt(arrival_times[2])) {
                            add_to_list = false;
                        }

                        if (add_to_list) {
                            // System.out.println(data);
                            s.add(data);
                        }
                        add_to_list = true;

                    }
                }
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (s.size() == 0) {
                System.out.println("no data was found for the inserted arrival time.");
                String[] a = null;

                System.out.println("Stopfinder ran sucessfully, with an error");
                this.error = true;
                return a;

            } else {
                String[] res = new String[s.size()];
                s.toArray(res);
                System.out.println("Stopfinder ran sucessfully, with no errors");
                return mergeSort(res);

            }
        }
        System.out.println("invalid time was entered");
        return null;
    }

    // public static void main(String[] args) {
    // String[] a = getStopByArival("12:12:10");
    // for (int i = 0; i < a.length; i++) {
    // System.out.println(a[i]);
    // }

    // }

    public static String[] mergeSort(String[] array) {
        if (array.length <= 1) {
            return array;
        }

        int mid = array.length / 2;
        String[] lo = new String[mid];
        String[] hi = new String[array.length - mid];

        for (int i = 0; i < hi.length; i++) {
            hi[i] = array[i];
        }
        for (int j = 0; j < lo.length; j++) {
            lo[j] = array[j + hi.length];
        }

        lo = mergeSort(lo);
        hi = mergeSort(hi);

        return merge(lo, hi);
    }

    private static String[] merge(String[] hi, String[] lo) {
        String[] result = new String[hi.length + lo.length];

        int i = 0;
        int j = 0;
        int k = 0;

        while (i < lo.length || j < hi.length) {
            if (i < lo.length && j < hi.length) {
                int lo_ptr = Integer.parseInt(lo[i].split(",")[0]);
                int hi_ptr = Integer.parseInt(hi[j].split(",")[0]);
                if (lo_ptr < hi_ptr) {
                    result[k++] = lo[i++];
                } else {
                    result[k++] = hi[j++];
                }
            } else if (i < lo.length) {
                result[k++] = lo[i++];
            } else if (j < hi.length) {
                result[k++] = hi[j++];
            }
        }

        return result;
    }
}