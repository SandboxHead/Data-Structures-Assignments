import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class FabricBreakup {
    public static Scanner sca;
    private int length=Integer.parseInt(sca.nextLine());
    private int[] pile=new int[length];
    int fav_value=0;
    private int index=0;
    public void push(int[] arr,int value){
        arr[index]=value;
        index++;
    }
    public int pop(int[] arr){
        if (index==0)return -1;
        index--;
        return arr[index];
    }
    public int lastValue(int[] arr){
        if (index==0)return 0;
        else return arr[index-1];
    }

    public static void main(String args[]) throws FileNotFoundException{
        File file=new File(args[0]);
        sca=new Scanner(file);
        FabricBreakup f = new FabricBreakup();
        int count=0;
        for (int i=0;i<f.length;i++){
            String[] arr = f.sca.nextLine().split(" ");
            if (arr[1].equals("1")){
                if (Integer.parseInt(arr[2])>f.fav_value){
                    f.fav_value=Integer.parseInt(arr[2]);
                    f.push(f.pile,Integer.parseInt(arr[2]));
                }
                else count++;
            }
            else{
                int myvalue=f.pop(f.pile);
                f.fav_value=f.lastValue(f.pile);
                if (myvalue==-1) System.out.println(arr[0]+" "+myvalue);
                else {
                    System.out.println(arr[0]+" "+count);
                    count=0;
                }
            }
        }
    }

}