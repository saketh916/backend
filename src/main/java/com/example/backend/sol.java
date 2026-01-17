package com.example.backend;
import java.util.*;

public class sol {
    public static void main(String args[]){
        int[] arr={1, 0, 6, 0, 1, 6};
        int total=0;
        for(int i:arr){
            total+=i;
        }
        int curSum=0;
        for(int i=0;i<arr.length;i++){
            curSum+=arr[i];
            if(curSum - arr[i] == total - curSum){
                System.out.println(i);
                break;
            }
        }
    }
}
