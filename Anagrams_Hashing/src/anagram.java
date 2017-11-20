import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.io.File;

public class anagram {
    private class node {
        public node next;
        public ArrayList<String> element;

        public node(node next, ArrayList<String> element) {
            this.element = element;
            this.next = next;
        }
    }
    public int collision;
    private int size;
    private node[] arr;

    private int HashFunction(String s) {
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            val = (val + (256 * val + (int)(s.charAt(i)))) % size;
        }
        return val;
    }

    public anagram(int size) {
        this.size = size;
        arr = new node[size];
        collision = 0;
    }

    private int value(char a) {
        if ((int) a == 39) return (int) a - 2;
        else if ((int) a < 58) return (int) a - 47;
        else return (int) a - 86;
    }

    private void insert(String now) {
        String sorted = sort(now);
        int val = HashFunction(sorted);
        if (arr[val] == null) {
            node n = new node(null, new ArrayList<>());
            n.element.add(sorted);
            n.element.add(now);
            arr[val] = n;
        }
        else {

            node n = arr[val];
            while (n.next != null) {
                collision++;
                if (n.element.get(0).equals(sorted)) break;
                n = n.next;
            }
            if (n.element.get(0).equals(sorted)) {
                if (!n.element.get(n.element.size() - 1).equals(now)) n.element.add(now);
            } else {
                node p = new node(null, new ArrayList<>());
                n.next = p;
                p.element.add(sorted);
                p.element.add(now);
            }
        }
    }

    private String sort(String s) {
        char[] c = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            c[i] = s.charAt(i);
        }
        Arrays.sort(c);
        return String.valueOf(c);
    }

    private List<String> findAnagrams(String s) {
        List<String> out = new ArrayList<>();
        String sorted = sort(s);
        int hash = HashFunction(sorted);
        node n = arr[hash];
        if (n != null) {
            while (n.next != null) {
                if (n.element.get(0).equals(sorted)) {
                    out.addAll(n.element.subList(1, n.element.size()));
                    //break;
                }
                n = n.next;
            }
            if (n.element.get(0).equals(sorted)) {
                out.addAll(n.element.subList(1, n.element.size()));
            }
        }
        return out;
    }

    private boolean search(String s) {
        String sorted = sort(s);
        int h = HashFunction(sorted);
        return true;
    }

    private void permutations3(String leader, int size, char[] branch, int level, List<String> out, String s, int max)
    {
        if (level >= size-1)
        {
            String found = String.valueOf(branch);
            List<String > out3 = findAnagrams(found);
            if (out3.size()!=0){
                int p =s.length();
                String sorted = sort(s);
                List<String> out4 = new ArrayList<>();
                for (int j = 3; j <= p / 2; j++) {
                    char[] branch2 = new char[j];
                    permutations2(sorted, j, branch2, -1, out4, sorted, p);
                }
                if(out4.size()!=0){
                    for(int x=0; x<out3.size(); x++){
                        for (int y = 0; y<out4.size(); y++){
                            out.add(out3.get(x)+" "+out4.get(y));
                        }
                    }
                }
            }
            return;
        }

        for (int i = 0; i < leader.length(); i++) {
            if (i > 0 && leader.charAt(i) == leader.charAt(i - 1)) {
                continue;
            }
            char temp = leader.charAt(i);
            branch[++level] = temp;
            String left = leader.substring(0, i + 1);
            int j = max-(level+leader.length()) + i;
            s =s.substring(0,j)+ s.substring(j+1);
            leader = leader.substring(i + 1);
            permutations3(leader, size, branch, level, out, s, max);
            leader = left + leader;
            s=s.substring(0,j)+temp+s.substring(j);
            level--;
        }
    }
    private void permutations2(String leader, int size, char[] branch, int level, List<String> out, String s, int max) {

        if (level >= size - 1) {
            String found = String.valueOf(branch);
            List<String> out1 = findAnagrams(found);
            if (out1.size()!=0 ) {
                List<String> out2 = findAnagrams(s);
                if (out2.size()!=0) {
                    for (int i = 0; i < out1.size(); i++) {
                        for (int j = 0; j < out2.size(); j++) {
                            out.add(out1.get(i) + " " + out2.get(j));
                            if (found.length() != s.length()) {
                                out.add(out2.get(j) + " " + out1.get(i));
                            }
                        }
                    }
                }
            }
            return;
        }

        for (int i = 0; i < leader.length(); i++) {
            if (i > 0 && leader.charAt(i) == leader.charAt(i - 1)) {
                continue;
            }
            char temp = leader.charAt(i);
            branch[++level] = temp;
            String left = leader.substring(0, i + 1);
            int j = max-(level+leader.length()) + i;
            s =s.substring(0,j)+ s.substring(j+1);
            leader = leader.substring(i + 1);
            permutations2(leader, size, branch, level, out, s, max);
            leader = left + leader;
            s=s.substring(0,j)+temp+s.substring(j);
            level--;
        }
    }

    public static void main(String args[]) throws IOException {
        long starttime = System.currentTimeMillis();
        File file = new File(args[0]);
        Scanner s = null;
        try {
            s = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int n = Integer.parseInt(s.nextLine());
        anagram a = new anagram(n);
        for (int i=0; i<n; i++){
            String now = s.nextLine();
            //System.out.println(now);
            a.insert(now);
        }
        System.out.println(a.collision);
        File file2 = new File(args[1]);
        Scanner here = null;
        try {
            here = new Scanner(file2);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int x = Integer.parseInt(here.nextLine());
        FileWriter writer = null;
        try {
            writer = new FileWriter("F:\\Studies\\COL106\\Assignments\\hashing\\src\\output1.txt");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        int total=0;
        for (int i=0; i<x; i++) {
            String now = here.nextLine();
            List<String> out = new ArrayList<>();
            if(now.length()<13 && now.length()>2) {
                out = a.findAnagrams(now);

                int p = now.length();
                String sorted = a.sort(now);
                if (p > 5) {
                    for (int j = 3; j <= p / 2; j++) {
                        char[] branch2 = new char[j];
                        a.permutations2(sorted, j, branch2, -1, out, sorted, p);
                    }
                }
                if (p == 9) {
                    char[] branch2 = new char[3];
                    a.permutations3(sorted, 3, branch2, -1, out, sorted, p);
                }
                if (p == 10) {
                    char[] branch2 = new char[3];
                    a.permutations3(sorted, 3, branch2, -1, out, sorted, p);
                    branch2 = new char[4];
                    a.permutations3(sorted, 4, branch2, -1, out, sorted, p);
                }
                if (p == 11) {
                    char[] branch2 = new char[3];
                    a.permutations3(sorted, 3, branch2, -1, out, sorted, p);
                    branch2 = new char[4];
                    a.permutations3(sorted, 4, branch2, -1, out, sorted, p);
                    branch2 = new char[5];
                    a.permutations3(sorted, 5, branch2, -1, out, sorted, p);
                }
                if (p == 12) {
                    char[] branch2 = new char[3];
                    a.permutations3(sorted, 3, branch2, -1, out, sorted, p);
                    branch2 = new char[4];
                    a.permutations3(sorted, 4, branch2, -1, out, sorted, p);
                    branch2 = new char[5];
                    a.permutations3(sorted, 5, branch2, -1, out, sorted, p);
                    branch2 = new char[6];
                    a.permutations3(sorted, 6, branch2, -1, out, sorted, p);
                }
            }
            //Collections.sort(out);
            int j;
            //writer.write(now+"\n");
            for (j=0; j<out.size();j++){
                //System.out.println(out.get(j));
                total++;
                writer.write(out.get(j)+"\n");

            }
            //System.out.println(-1);
            writer.write(-1+"\n");
            total++;
        }
        System.out.println(total);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - starttime;
        System.out.println(totalTime);
        writer.close();
    }
}

