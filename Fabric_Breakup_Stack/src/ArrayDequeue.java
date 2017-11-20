
public class ArrayDequeue implements DequeInterface {
  private Object[] arr = new Object[1];
  private int frontIndex=0;
  private int lastIndex=0;
  public void insertFirst(Object o){
    if (size()==arr.length-1){
      Object[] arr2=new Object[2*arr.length];
      for (int i=0;i<arr.length;i++){
        arr2[i]=arr[(i+frontIndex)%arr.length];
      }
      frontIndex=0;
      lastIndex=arr.length-1;
      arr=arr2;
    }
    arr[(arr.length+frontIndex-1)%arr.length]=o;
    frontIndex=(arr.length+frontIndex-1)%arr.length;
    //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
  }
  
  public void insertLast(Object o){
    if (size()==arr.length-1) {
      Object[] arr2 = new Object[2 * arr.length];
      for (int i = 0; i < arr.length-1; i++) {
        arr2[i] = arr[(i + frontIndex) % arr.length];
      }
      frontIndex=0;
      lastIndex = arr.length - 1;
      arr = arr2;
    }
    arr[lastIndex]=o;
    lastIndex=(lastIndex+1)%arr.length;
    //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    
  }
  
  public Object removeFirst() throws EmptyDequeException{
      if (isEmpty()) throw new EmptyDequeException("The dequeue is empty.");
    frontIndex=(frontIndex+1)%arr.length;
    return arr[frontIndex];
    //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
  }
  
  public Object removeLast() throws EmptyDequeException{

    if (isEmpty()) throw new EmptyDequeException("The dequeue is empty.");
    lastIndex=(arr.length+lastIndex-1)%arr.length;
    return arr[lastIndex];
    //throw new  java.lang.UnsupportedOperationException("Not implemented yet.");
  }
  public Object first() throws EmptyDequeException{
      if (isEmpty()) throw new EmptyDequeException("The dequeue is empty.");
    return arr[frontIndex];
    //throw new  java.lang.UnsupportedOperationException("Not implemented yet.");
  }
  
  public Object last() throws EmptyDequeException{
      if (isEmpty()) throw new EmptyDequeException("The dequeue is empty.");
      return arr[lastIndex-1];
    //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
  }
  
  public int size(){
    return (arr.length+lastIndex-frontIndex)%arr.length;
    //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
  }
  public boolean isEmpty(){
    return (frontIndex==lastIndex);
    //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
  }
  public String toString(){
    if (frontIndex==lastIndex)return "[]";
    String output="[";
    int index=frontIndex;
    while (index!=lastIndex){
      output=output.concat(arr[index].toString());
      output=output.concat(", ");
      index=(index+1)%arr.length;
    }
    output= output.substring(0,output.length()-2);
    output=output.concat("]");
    return output;
    //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
  }
  
  
  public static void main(String[] args){
    int  N = 10;
    DequeInterface myDeque = new ArrayDequeue();
    for(int i = 0; i < N; i++) {
      myDeque.insertFirst(i);
      myDeque.insertLast(-1*i);
    }
   
    int size1 = myDeque.size();
    System.out.println("Size: " + size1);
    System.out.println(myDeque.toString());
    
    if(size1 != 2*N){
      System.err.println("Incorrect size of the queue.");
    }
    
    //Test first() operation
    try{
      int first = (int)myDeque.first();
      int size2 = myDeque.size(); //Should be same as size1
      if(size1 != size2) {
        System.err.println("Error. Size modified after first()");
      }
    }
    catch (EmptyDequeException e){
      System.out.println("Empty queue");
    }
    
    //Remove first N elements
    for(int i = 0; i < N; i++) {
      try{
        int first = (Integer)myDeque.removeFirst();
      }
      catch (EmptyDequeException e) {
        System.out.println("Cant remove from empty queue");
      }
      
    }
    
    
    int size3 = myDeque.size();
    System.out.println("Size: " + myDeque.size());
    System.out.println(myDeque.toString());
    
    if(size3 != N){
      System.err.println("Incorrect size of the queue.");
    }
    
    try{
      int last = (int)myDeque.last();
      int size4 = myDeque.size(); //Should be same as size3
      if(size3 != size4) {
        System.err.println("Error. Size modified after last()");
      }
    }
    catch (EmptyDequeException e){
      System.out.println("Empty queue");
    }
    
    //empty the queue  - test removeLast() operation as well
    while(!myDeque.isEmpty()){
        try{
          int last = (int)myDeque.removeLast();
        }
        catch (EmptyDequeException e) {
          System.out.println("Cant remove from empty queue");
        }
    }
    
    int size5 = myDeque.size();
    if(size5 != 0){
      System.err.println("Incorrect size of the queue.");
    }
    
  }
  
}