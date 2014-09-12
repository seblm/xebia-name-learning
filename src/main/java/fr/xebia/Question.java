package fr.xebia;

public class Question {
    public final int first;
    public final int second;
    public final boolean firstIsAnswer;

    public Question(int first, int second, boolean firstIsAnswer) {
        this.first = first;
        this.second = second;
        this.firstIsAnswer = firstIsAnswer;
    }
}
