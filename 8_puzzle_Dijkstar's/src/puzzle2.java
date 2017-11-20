import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class puzzle2 {
    private static class minPQ<Item extends Comparable<Item>>{
        private Item[] arr;
        private int size;
        public minPQ(){
            arr = (Item[]) new Comparable[181450];
            arr[0]=null;
        }
        public boolean isEmpty(){
            return size==0;
        }
        public void insert(Item key){
            arr[size+1]=key;
            size++;
            percUp(size);
        }
        public Item delMin(){
            Item min = arr[1];
            if(!(size==1)) {
                arr[1]=arr[size];
            }
            size--;
            percDown(1);
            return min;
        }
        private void percUp(int k){
            while(k>1 && arr[k/2].compareTo(arr[k])>0){
                Item start = arr[k];
                arr[k] = arr[k/2];
                arr[k/2] = start;
                k=k/2;
            }
        }
        private void percDown(int k){
            while(2*k <= size){
                int j = 2*k;
                if(j<size && arr[j].compareTo(arr[j+1])>0) j++;
                if(arr[k].compareTo(arr[j])<=0) break;
                Item last = arr[j];
                arr[j] = arr[k];
                arr[k] = last;
                k=j;
            }
        }
    }
    public static class info{
        //public ArrayList<Board> neighbours = new ArrayList<>();
        public Board previous;
        public Double pathLength;
        public Boolean found;
        public int moves;
        public info( Board previous, double pathLength, boolean found, int moves){
            //this.neighbours = neighbours;
            this.pathLength =  pathLength;
            this.previous = previous;
            this.found = found;
            this.moves = moves;
        }
    }
    public static class Board implements Comparable<Board> {
        public String  own;
        private String swap(String s, int i, int j) {
            if (j != s.length() - 1)
                return s.substring(0, i) + s.substring(j, j + 1) + s.substring(i + 1, j) + s.substring(i, i + 1) + s.substring(j + 1);
            else return s.substring(0, i) + s.substring(j, j + 1) + s.substring(i + 1, j) + s.substring(i, i + 1);
        }
        public ArrayList<String> board = new ArrayList<>();
        public HashMap<Board, info> h;
        public Board(String s, HashMap<Board, info> h){
            this.own = s;
            Collections.addAll(board, s.split(""));
            this.h = h;
        }

        public ArrayList<Board> neighbours() {
            ArrayList<Board> neis = new ArrayList<>();
            int index = this.board.indexOf("G");
            if (index < 6) {
                Board up = new Board(own, h);
                Collections.swap(up.board, index, index + 3);
                up.own = swap(up.own, index, index + 3);
                neis.add(up);
//                neis.add(new Neighbour(up, this, value, index + 3, "U"));
            }
            if (index > 2) {
                Board down = new Board(own, h);
                Collections.swap(down.board, index, index - 3);
                down.own = swap(down.own, index - 3, index);
                neis.add(down);
//                neis.add(new Neighbour(down, this, value, index - 3, "D"));
            }
            if (index % 3 != 2) {
                Board left = new Board(own,h);
                Collections.swap(left.board, index, index + 1);
                left.own = swap(left.own, index, index + 1);
                neis.add(left);
//                neis.add(new Neighbour(left, this, value, index + 1, "L"));
            }
            if (index % 3 != 0) {
                Board right = new Board(own,h);
                Collections.swap(right.board, index, index - 1);
                right.own = swap(right.own, index - 1, index);
                neis.add(right);
//                neis.add(new Neighbour(right, this, value, index - 1, "R"));
            }
            return neis;
        }
        public int distance(Board that, int[] value){
            int index = this.board.indexOf("G");
            return value[Integer.parseInt(that.board.get(index))-1];
        }
        public String toString() {
            return own.substring(0, 3) + "\n" + own.substring(3, 6) + "\n" + own.substring(6);
        }
        @Override
        public boolean equals(Object o){
            Board that = (Board) o;
            return (this.own.equals(that.own));
        }
        @Override
        public int hashCode(){
            return this.own.hashCode();
        }

        @Override
        public int compareTo(Board o) {
            info one =h.get(this);
            info two = h.get(o);
            if(one.pathLength.compareTo(two.pathLength)==0){
                int moves1 = one.moves;
                int moves2 = two.moves;
                if(moves1>moves2) return 1;
                else if(moves1<moves2) return -1;
                else return 0;

            }
            return one.pathLength.compareTo(two.pathLength);
        }
        public String move(Board that){
            int index1 = this.board.indexOf("G");
            int index2 = that.board.indexOf("G");
            String charac = that.board.get(index1);
            if(index1==index2+1){
                charac = charac+"R";
            }
            else if (index1==index2-1)charac = charac+"L";
            else if (index1 == index2+3)charac = charac + "D";
            else if (index1 == index2-3)charac = charac + "U";
            return charac;
        }
    }
    public static int[] intarr(String s) {
        int[] arr = new int[8];
        String[] arr2 = s.split(" ");
        for (int i = 0; i < 8; i++) {
            arr[i] = Integer.parseInt(arr2[i]);
        }
        return arr;
    }
    public static void permutation(String str, HashMap<Board, info> h) {
        permutation("", str, h);
    }

    private static void permutation(String prefix, String str, HashMap<Board, info> h) {
        int n = str.length();
        if (n == 0) {
            Board b = new Board(prefix, h);
            info in = new info( null, Double.POSITIVE_INFINITY, false ,0);
            h.put(b, in);
        }
        else {
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n),h);
        }
    }
    public static void main(String args[]) throws IOException {
        long start = System.currentTimeMillis();
        File input = new File(args[0]);
        File output = new File(args[1]);
        Scanner s = new Scanner(input);
        int total = Integer.parseInt(s.nextLine());
        HashMap<Board, info> map = new HashMap<>();
        FileWriter writer = null;
        long start2 = System.currentTimeMillis();
        permutation("12345678G",map);
        System.out.println(System.currentTimeMillis()-start2);
        try {
            writer = new FileWriter(output);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        long stopclean2 = 0;
        for(int i=0; i<total; i++) {
            String[] boards = s.nextLine().split(" ");
            int[] values = intarr(s.nextLine());
            Board source = new Board(boards[0], map);
            Board destination = new Board(boards[1], map);
            if (source.equals(destination)) {
                writer.write("0 0\n\n");
                continue;
            }
            int count = 0;
            for (int j = 0; j < 9; j++) {
                char one = source.own.charAt(j);
                if (!(one == 'G')) {
                    for (int k = j + 1; k < 9; k++) {
                        char two = source.own.charAt(k);
                        if (!(two == 'G')) {
                            for (int p = 0; p < 9; p++) {
                                char three = destination.own.charAt(p);
                                if (three == one) {
                                    int q;
                                    for (q = p + 1; q < 9; q++) {
                                        char four = destination.own.charAt(q);
                                        if (four == two) break;
                                    }
                                    if (q == 9) count++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (count % 2 == 1) {
                writer.write("-1 -1\n\n");
                continue;
            }
            map.get(source).pathLength = (double) 0;
            map.get(source).previous = null;
            map.get(source).found = true;
            minPQ<Board> pq = new minPQ<>();
            long startclean = System.currentTimeMillis();
            long stopclean = 0;
            long stopclean1 = 0;

            long stopclean3 = 0;
            pq.insert(source);
            int count1 = 0, count2 = 0;
            while(pq.size!=0) {
                long curpq = System.currentTimeMillis();
                Board current = pq.delMin();
                count1++;
                stopclean2 = stopclean2 + System.currentTimeMillis()-curpq;
                if (current.own.equals(destination.own)) {
                    long curr = System.currentTimeMillis();
                    info in = map.get(current);
                    String out = "";
                    int len = 0;
                    ArrayList<String> arr2 = new ArrayList<>();
                    while(!(map.get(current).previous==null)){
                        arr2.add(map.get(current).previous.move(current));
                        //out = out + map.get(current).previous.move(current) +" ";
                        current = map.get(current).previous;
                        len++;
                    }
                    for(int p=arr2.size()-1; p>=0;p--){
                        out = out.concat(arr2.get(p)+" ");
                    }
                    out = out.substring(0,out.length()-1);
                    writer.write(len+" "+in.pathLength.intValue()+"\n");
                    writer.write(out+"\n");
                    stopclean1 = stopclean1+System.currentTimeMillis()-curr;
                    break;
                }

                map.get(current).found = true;
                info curr = map.get(current);
                long curre = System.currentTimeMillis();
                ArrayList<Board> neighbours = current.neighbours();
                stopclean  = stopclean+System.currentTimeMillis()-curre;
                for (int j = 0; j < neighbours.size(); j++) {
                    Board neigh = neighbours.get(j);
                    info nei = map.get(neigh);
                    if (!nei.found) {
                        Double dist = curr.pathLength + (double) current.distance(neigh, values);
                        if (dist.compareTo(nei.pathLength) < 0) {
                            nei.pathLength = dist;
                            nei.previous = current;
                            nei.moves = curr.moves + 1;
                            long currr = System.currentTimeMillis();
                            pq.insert(neigh);
                            count2++;
                            stopclean3 = stopclean3 + System.currentTimeMillis()-currr;
                        }
                    }
                }
            }
            System.out.println(stopclean);
            //System.out.println(stopclean3);
            //System.out.println(count1);
            //System.out.println(count2);
            Set<Board> boar = map.keySet();
            for(Board b: boar){
                info inf = map.get(b);
                inf.previous = null;
                inf.pathLength = Double.POSITIVE_INFINITY;
                inf.found = false;
            }

        }
        writer.close();
        long stop = System.currentTimeMillis();
        System.out.println(stop-start);
    }

}