import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;

public class Puzzle {
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
    private static class Neighbour{
        private Board board;
        private String move;
        //private Board father;
        private int distance;
        public Neighbour(Board b, Board parent, int[] value, int i, String s){
            this.board = b;
            int cha = Integer.parseInt(parent.board.get(i));
            this.distance = value[cha-1];
            this.move = Integer.toString(cha) + s;
        }
        public String toString(){
            return board.toString()+" "+move+" "+ distance;
        }
    }
    private static class pathDis{
        public Double distance;
        public ArrayList<String> path = new ArrayList<>();
        public boolean found;
        private pathDis(int distance, String move, Boolean found){
            this.distance = (double)distance;
            this.path.add(move);
            this.found = found;
        }
        public String toString(){
            return path +" "+ Double.toString(distance) + " " + found;
        }
    }
    public static class Board implements Comparable<Board>{
        private ArrayList<String> board = new ArrayList<>();
        private String own;
        private int[] value;
        private HashMap<Board, pathDis> h;

        private Board(String s, int[] value, HashMap<Board, pathDis> h) {
            for (int i = 0; i < s.length(); i++) {
                this.board.add(s.substring(i, i + 1));
            }
            this.own = s;
            this.value = value;
            this.h = h;
        }

        private String swap(String s, int i, int j) {
            if (j != s.length() - 1)
                return s.substring(0, i) + s.substring(j, j + 1) + s.substring(i + 1, j) + s.substring(i, i + 1) + s.substring(j + 1);
            else return s.substring(0, i) + s.substring(j, j + 1) + s.substring(i + 1, j) + s.substring(i, i + 1);
        }

        public Neighbour[] neighbour() {
            Neighbour[] neis = new Neighbour[4];
            int i = 0;
            int index = this.board.indexOf("G");
            if (index < 6) {
                Board up = new Board(own, value, h);
                Collections.swap(up.board, index, index + 3);
                up.own = swap(up.own, index, index + 3);
                //neis.add(up);
                neis[i]=new Neighbour(up, this, value, index + 3, "U");
                i++;
            }
            if (index > 2) {
                Board down = new Board(own, value, h);
                Collections.swap(down.board, index, index - 3);
                down.own = swap(down.own, index - 3, index);
//                neis.add(down);
                neis[i++]=new Neighbour(down, this, value, index - 3, "D");
            }
            if (index % 3 != 2) {
                Board left = new Board(own, value, h);
                Collections.swap(left.board, index, index + 1);
                left.own = swap(left.own, index, index + 1);
//                neis.add(left);
                neis[i++]=new Neighbour(left, this, value, index + 1, "L");
            }
            if (index % 3 != 0) {
                Board right = new Board(own, value, h);
                Collections.swap(right.board, index, index - 1);
                right.own = swap(right.own, index - 1, index);
//                neis.add(right);
                neis[i]=new Neighbour(right, this, value, index - 1, "R");
            }
            return neis;
        }

        public String toString() {
            return own.substring(0, 3) + "\n" + own.substring(3, 6) + "\n" + own.substring(6);
        }

        public int distance(Board that, Neighbour[] n) {
            for (int i = 0; i < 4; i++) {
                if (n[i].board.own.equals(that.own)) {
                    return n[i].distance;
                }
            }
            return -1;
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
            if(h.get(this).distance.compareTo(h.get(o).distance)==0){
                if(h.get(this).path.size()>h.get(o).path.size()){
                    return 1;
                }
                else if(h.get(this).path.size()<h.get(o).path.size()){
                    return -1;
                }
                else return 0;

            }
            return h.get(this).distance.compareTo(h.get(o).distance);
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
    public static void main(String args[]) throws IOException {
//        int[] arr = {1,2,3,4,5,6,7,8};
//        Board a = new Board("1234G5678",arr);
//        System.out.println(a+"\n");
//        a.neighbour().forEach(System.out::println);
        long start = System.currentTimeMillis();
        File input = new File(args[0]);
        File output = new File(args[1]);
        Scanner s = new Scanner(input);
        int total = Integer.parseInt(s.nextLine());
        HashMap<Board, pathDis> cloud = new HashMap<>();
        FileWriter writer = null;
        try {
            writer = new FileWriter(output);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0; i<total; i++){

            String[] boards = s.nextLine().split(" ");
            int[] values = intarr(s.nextLine());
            Board source = new Board(boards[0], values, cloud);
            Board destination = new Board(boards[1], values, cloud );
            if(source.equals(destination)){
                writer.write("0 0\n\n");
                continue;
            }
            int count = 0;
            for(int j=0; j<9; j++){
                char one = source.own.charAt(j);
                if(!(one == 'G')) {
                    for (int k = j+1; k < 9; k++) {
                        char two = source.own.charAt(k);
                        if(!(two=='G')) {
                            for (int p = 0; p < 9; p++) {
                                char three = destination.own.charAt(p);
                                if (three == one) {
                                    int q;
                                    for (q = p+1; q < 9; q++) {
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
            if (count%2==1){
                writer.write("-1 -1\n\n");
                continue;
            }
            if(!cloud.containsKey(source)){
                cloud.put(source, new pathDis(0, null, true));
            }
            else {
                pathDis p = cloud.get(source);
                p.found = true;
                p.path = null;
                p.distance =(double) 0;
            }
            int pathLength = 0;
            String path = "";
            Board current = new Board(boards[0], values, cloud);

            Neighbour[] nei = current.neighbour();
            minPQ<Board> pq = new minPQ<>();
            int size;
            for(size=0; size<4; size++){
                if(nei[size]==null) break;
            }
            for (int j=0; j<size; j++){
                Board neighbour = nei[j].board;
                if(!cloud.containsKey(neighbour)) {
                    pathDis p = new pathDis(current.distance(neighbour, nei),nei[j].move,false);
                    cloud.put(neighbour, p);
                }
                else {
                    pathDis p = cloud.get(neighbour);
                    p.found = false;
                    p.path.add(nei[j].move);
                    p.distance =(double) current.distance(neighbour, nei);
                }
                pq.insert(neighbour);

            }
            long startclean = System.currentTimeMillis();
            long stopclean = 0;
            long stopclean1 = 0;
            long stopclean2 = 0;
            long stopclean3 = 0;
            while(pq.size!=0){
                current = pq.delMin();
                pathDis currentInfo = cloud.get(current);
                if(currentInfo.found)continue;
                long curre = System.currentTimeMillis();
                nei = current.neighbour();
                stopclean1 = stopclean1+ System.currentTimeMillis()-curre;
                if(current.own.equals(destination.own)){
                    writer.write( currentInfo.path.size()+" "+currentInfo.distance.intValue()+"\n");
                    String out = "";
                    for(String g: currentInfo.path){
                        out = out.concat(g+" ");
                    }
                    writer.write(out.substring(0, out.length()-1)+"\n");
                    break;
                }
                currentInfo.found = true;
                int j;
                for(size=0; size<4; size++){
                    if(nei[size]==null) break;
                }
                for (j=0; j<size; j++){
                    long curr = System.currentTimeMillis();
                    Board neighbour = nei[j].board;
                    if(!cloud.containsKey(neighbour)){
                        //long currt = System.currentTimeMillis();
                        pathDis p = new pathDis(current.distance(neighbour, nei)+currentInfo.distance.intValue(),nei[j].move,false);
                        //stopclean2 = stopclean2 + System.currentTimeMillis()-currt;
                        //currt = System.currentTimeMillis();
                        String str = p.path.get(0);
                        p.path = new ArrayList<>();
                        p.path.addAll(currentInfo.path);
                        p.path.add(str);
                        cloud.put(neighbour, p);
                        //stopclean3 = stopclean3 + System.currentTimeMillis()-currt;
                        pq.insert(neighbour);
                    }


                    else if(!cloud.get(neighbour).found){
                        pathDis neighinfo = cloud.get(neighbour);
                        if(!neighinfo.distance.equals(Double.POSITIVE_INFINITY) ){
                            if(neighinfo.distance.compareTo(currentInfo.distance+current.distance(neighbour, nei))>0){
                                neighinfo.distance = currentInfo.distance + current.distance(neighbour, nei);
                                neighinfo.path.add(nei[j].move);
                                pq.insert(neighbour);
                            }
                        }
                        else {
                            //long curr = System.currentTimeMillis();
                            pathDis p = neighinfo;
                            p.found = false;
                            p.path.addAll(currentInfo.path);
                            p.path.add(nei[j].move);
                            p.distance = (double) current.distance(neighbour, nei) + currentInfo.distance;
                            pq.insert(neighbour);
                        }
                    }
                    stopclean = stopclean + System.currentTimeMillis()-curr;
                }

            }
            //System.out.println(System.currentTimeMillis()-startclean);
            //System.out.println(stopclean);
            //System.out.println(stopclean1);
            //System.out.println(stopclean2);
            //System.out.println(stopclean3);
            System.out.println(System.currentTimeMillis()-startclean);
            Set<Board> b = cloud.keySet();
            //System.out.println(b.size());
            for(Board board : b){
                pathDis p = cloud.get(board);
                p.distance = Double.POSITIVE_INFINITY;
                p.path = new ArrayList<>();
                p.found = false;
            }
            boolean ran =true;

        }
        writer.close();
//        minPQ<Integer> m = new minPQ<>();
//        for (int x=50; x>0; x--){
//            m.insert(x);
//        }
//        for(int x=0; x<50; x++){
//            System.out.println(m.delMin());
//        }
        long stop = System.currentTimeMillis();
//
        System.out.println(stop - start);
    }
}
