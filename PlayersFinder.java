import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
interface IPlayersFinder {
    /**
     * Search for players locations at the given photo
     * @param photo
     *     Two dimension array of photo contents
     *     Will contain between 1 and 50 elements, inclusive
     * @param team
     *     Identifier of the team
     * @param threshold
     *     Minimum area for an element
     *     Will be between 1 and 10000, inclusive
     * @return
     *     Array of players locations of the given team
     */
    java.awt.Point[] findPlayers(String[] photo, int team, int threshold);
}


public class PlayersFinder implements IPlayersFinder{
    static int minRows=0 , maxRows=0 , minCol=0 , maxCol=0 , counter = 0;
    static int index=0;
    final char zeroAscii='0';
    public java.awt.Point[] findPlayers(String[] photo, int team, int threshold){
        char[][] playGround = new char[photo.length][photo[0].length()];
        java.awt.Point[] solution = new java.awt.Point[100];
        char teamPlayer = (char) (team + zeroAscii);
        for(int i=0 ; i< photo.length ; i++){
            for(int j=0 ; j<photo[0].length() ; j++){
                playGround[i][j] = photo[i].charAt(j);
            }
        }

        for(int i=0 ; i< photo.length ; i++){
            for(int j=0 ; j< photo[0].length() ; j++){
                if(playGround[i][j] == teamPlayer) {
                    SetVariables(i,j);
                    GetPlayer(playGround, teamPlayer, i, j);
                    if (counter*4 >= (threshold)) {
                        solution[index] = new java.awt.Point(minCol + maxCol + 1, minRows + maxRows + 1);
                        index++;
                    }
                }
            }
        }

        return solution;
    }
  void GetPlayer(char[][] playGround, int teamPlayer , int i, int j){
        if((i < playGround.length) && (i >= 0) && (j >= 0) && (j < playGround[0].length) ){
            if(playGround[i][j]==teamPlayer) {
                playGround[i][j] = 'A';
                counter++;
                maxRows = max(i, maxRows);
                minRows = min(i, minRows);
                maxCol = max(j, maxCol);
                minCol = min(j, minCol);

                GetPlayer(playGround, teamPlayer, i , j+1);
                GetPlayer(playGround, teamPlayer, i - 1, j);
                GetPlayer(playGround, teamPlayer, i + 1, j);
                GetPlayer(playGround, teamPlayer, i, j - 1);
            }
        }
    }

    public void SetVariables(int i , int j){
        minRows = i;
        maxRows = i;
        minCol = j;
        maxCol = j;
        counter = 0;
    }

    public int max(int i , int j){
        if(i > j)
            return i;
        else
            return j;
    }
    public int min(int i , int j){
        if(i < j)
            return i;
        else
            return j;
    }

    public  java.awt.Point[] sort(java.awt.Point[] finalSolution, int index){
        java.awt.Point temp=new java.awt.Point(0,0);
        for(int i=0 ; i < index ; i++){
            for (int j=0 ; j < index - 1 - i ; j++){
                if(finalSolution[j].getX() > finalSolution[j+1].getX()){
                    temp = finalSolution[j];
                    finalSolution[j] = finalSolution[j+1];
                    finalSolution[j+1] = temp;
                }
                else if(finalSolution[j].getX() == finalSolution[j+1].getX()){
                    if(finalSolution[j].getY() > finalSolution[j+1].getY()){
                        temp = finalSolution[j];
                        finalSolution[j] = finalSolution[j+1];
                        finalSolution[j+1] = temp;
                    }
                }
            }
        }
        return finalSolution;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String firstLine = sc.nextLine();
        String[] s = firstLine.split(", ");
        int[] array = new int[s.length];
        if(s.length==1 && s[0].isEmpty()) {
            array = new int[]{};
        }else {
            for(int k=0; k<s.length; k++) {
                array[k]=Integer.parseInt(s[k]);
            }
        }
        String[] photo = new String[array[0]];
        for (int i=0; i < array[0]; i++){
            photo[i] = sc.nextLine();
        }
        int team = sc.nextInt();
        int threshold = sc.nextInt();
        java.awt.Point[] finalSolution = new PlayersFinder().findPlayers(photo, team, threshold);
        if(finalSolution==null){
            System.out.printf("[]");
        }
        else {
            finalSolution = new PlayersFinder().sort(finalSolution,index);
            System.out.printf("[");
            for (int i = 0; i < index; i++) {
                System.out.printf("(%d, %d)", (int) finalSolution[i].getX(), (int) finalSolution[i].getY());
                if (i < index - 1) {
                    System.out.printf(", ");
                }
            }
            System.out.printf("]");
        }
    }
}

