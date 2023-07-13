package server;

public class Pair<F,S> {
    private F first;
    private S second;

    public Pair(){
        this.first = null;
        this.second = null;
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public boolean isFull(){
        return this.first != null && this.second != null;
    }

    public boolean hasFirst(){
        return this.first != null;
    }

    public boolean hasSecond(){
        return this.second != null;
    }


    public F getFirst() {
        return first;
    }



    public void setFirst(F first) {
        this.first = first;
    }



    public S getSecond() {
        return second;
    }



    public void setSecond(S second) {
        this.second = second;
    }
    
    
}
