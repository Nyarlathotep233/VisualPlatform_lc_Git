package com.reinforcement.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 
 * QLearning.java Create on 2017年9月4日 下午10:08:49    
 *    
 * 类功能说明:   QLearning简明例子实现
 *
 * Copyright: Copyright(c) 2013 
 * Company: COSHAHO
 * @Version 1.0
 * @Author coshaho
 */
public class TestE 
{
    FeedbackMatrix R = new FeedbackMatrix();
    
    ExperienceMatrix Q = new ExperienceMatrix();
    
    public static void main(String[] args)
    {
        TestE ql = new TestE();
        
        for(int i = 0; i < 100; i++)
        {
            Random random = new Random();
            int x = random.nextInt(100) % 6;
            
            System.out.println("第" + i + "次学习, 初始房间是" + x);
            ql.learn(x);
            System.out.println();
        }
    }
    
    public void learn(int x)
    {
        do
        {
            // 随机选择一个联通的房间进入
            int y =  chooseRandomRY(x);
            
            // 获取以进入的房间为起始点的历史最佳得分
            int qy = getMaxQY(y);
            
            // 计算此次移动的得分
            int value = calculateNewQ(x, y, qy);
            Q.set(x, y, value);
            x = y;
        }
        // 走出房间则学习结束
        while(5 != x);
        
        Q.print();
    }
    
    public int chooseRandomRY(int x)
    {
        int[] qRow = R.getRow(x);
        List<Integer> yValues = new ArrayList<Integer>();
        for(int i = 0; i < qRow.length; i++)
        {
            if(qRow[i] >= 0)
            {
                yValues.add(i);
            }
        }

        Random random = new Random();
        int i = random.nextInt(yValues.size()) % yValues.size();
        return yValues.get(i);
    }
    
    public int getMaxQY(int x)
    {
        int[] qRow = Q.getRow(x);
        int length = qRow.length;
        List<YAndValue> yValues = new ArrayList<YAndValue>();
        for(int i = 0; i < length; i++)
        {
            YAndValue yv = new YAndValue(i, qRow[i]);
            yValues.add(yv);
        }
        
        Collections.sort(yValues);
        int num = 1;
        int value = yValues.get(0).getValue();
        for(int i = 1; i < length; i++)
        {
            if(yValues.get(i).getValue() == value)
            {
                num = i + 1;
            }
            else
            {
                break;
            }
        }
        
        Random random = new Random();
        int i = random.nextInt(num) % num;
        return yValues.get(i).getY();
    }
    
    // Q(x,y) = R(x,y) + 0.8 * max(Q(y,i))
    public int calculateNewQ(int x, int y, int qy)
    {
        return (int) (R.get(x, y) + 0.8 * Q.get(y, qy));
    }
    
    public static class YAndValue implements Comparable<YAndValue>
    {
        int y;
        int value;
        
        public int getY() {
            return y;
        }
        public void setY(int y) {
            this.y = y;
        }
        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }
        public YAndValue(int y, int value)
        {
            this.y = y;
            this.value = value;
        }
        public int compareTo(YAndValue o) 
        {
            return o.getValue() - this.value;
        }
    }
}


/**
 * 
 * FeedbackMatrix.java Create on 2017年9月4日 下午9:52:41    
 *    
 * 类功能说明:   反馈矩阵
 *
 * Copyright: Copyright(c) 2013 
 * Company: COSHAHO
 * @Version 1.0
 * @Author coshaho
 */
class FeedbackMatrix 
{
    public int get(int x, int y)
    {
        return R[x][y];
    }
    
    public int[] getRow(int x)
    {
        return R[x];
    }
    
    private static int[][] R = new int[6][6];
    static 
    {
        R[0][0] = -1;
        R[0][1] = -1;
        R[0][2] = -1;
        R[0][3] = -1;
        R[0][4] = 0;
        R[0][5] = -1;
        
        R[1][0] = -1;
        R[1][1] = -1;
        R[1][2] = -1;
        R[1][3] = 0;
        R[1][4] = -1;
        R[1][5] = 100;
        
        R[2][0] = -1;
        R[2][1] = -1;
        R[2][2] = -1;
        R[2][3] = 0;
        R[2][4] = -1;
        R[2][5] = -1;
        
        R[3][0] = -1;
        R[3][1] = 0;
        R[3][2] = 0;
        R[3][3] = -1;
        R[3][4] = 0;
        R[3][5] = -1;
        
        R[4][0] = 0;
        R[4][1] = -1;
        R[4][2] = -1;
        R[4][3] = 0;
        R[4][4] = -1;
        R[4][5] = 100;
        
        R[5][0] = -1;
        R[5][1] = 0;
        R[5][2] = -1;
        R[5][3] = -1;
        R[5][4] = 0;
        R[5][5] = 100;
    }
}


/**
 * 
 * ExperienceMatrix.java Create on 2017年9月4日 下午10:03:08    
 *    
 * 类功能说明:   经验矩阵
 *
 * Copyright: Copyright(c) 2013 
 * Company: COSHAHO
 * @Version 1.0
 * @Author coshaho
 */
class ExperienceMatrix 
{
    public int get(int x, int y)
    {
        return Q[x][y];
    }
    
    public int[] getRow(int x)
    {
        return Q[x];
    }
    
    public void set(int x, int y, int value)
    {
        Q[x][y] = value;
    }
    
    public void print()
    {
        for(int i = 0; i < 6; i++)
        {
            for(int j = 0; j < 6; j++)
            {
                String s = Q[i][j] + "  ";
                if(Q[i][j] < 10)
                {
                    s = s + "  ";
                }
                else if(Q[i][j] < 100)
                {
                    s = s + " ";
                }
                System.out.print(s);
            }
            System.out.println();
        }
    }
    
    private static int[][] Q = new int[6][6];
    static
    {
        Q[0][0] = 0;
        Q[0][1] = 0;
        Q[0][2] = 0;
        Q[0][3] = 0;
        Q[0][4] = 0;
        Q[0][5] = 0;
        
        Q[1][0] = 0;
        Q[1][1] = 0;
        Q[1][2] = 0;
        Q[1][3] = 0;
        Q[1][4] = 0;
        Q[1][5] = 0;
        
        Q[2][0] = 0;
        Q[2][1] = 0;
        Q[2][2] = 0;
        Q[2][3] = 0;
        Q[2][4] = 0;
        Q[2][5] = 0;
        
        Q[3][0] = 0;
        Q[3][1] = 0;
        Q[3][2] = 0;
        Q[3][3] = 0;
        Q[3][4] = 0;
        Q[3][5] = 0;
        
        Q[4][0] = 0;
        Q[4][1] = 0;
        Q[4][2] = 0;
        Q[4][3] = 0;
        Q[4][4] = 0;
        Q[4][5] = 0;
        
        Q[5][0] = 0;
        Q[5][1] = 0;
        Q[5][2] = 0;
        Q[5][3] = 0;
        Q[5][4] = 0;
        Q[5][5] = 0;
    }
}