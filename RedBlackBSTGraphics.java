/* *****************************************************************************
    Name: Rocky and Eli
    Date: 12-11-19
    Description: graphics for the red black bst, the program
    periodically adds a random number to the bst and draws it.
 **************************************************************************** */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;

public class RedBlackBSTGraphics {

    private static RedBlackBST BST = new RedBlackBST();
    //window dimensions
    private static int width = 1200;
    private static int height = 800;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndRunGraphics();
            }
        });
    }

    private static void createAndRunGraphics(){
        System.out.println("Created GUI on EDT? "+ SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Red Black BST Graphics");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new BSTPanel(BST, width, height));
        f.setSize(width, height);
        f.setVisible(true);
    }


}

class BSTPanel extends JPanel implements ActionListener {

    private RedBlackBST BST;
    private Timer animator;
    //animation delay, the period of time between each random inserton and graphics update
    private int delay = 1000;
    //node diameter for the graphics
    private int nodeDiam = 60;
    private int treeHeight;
    private int treeWidth;
    //for making random insertions
    private Integer[] randomNums = new Integer[256];
    private int index = 0;

    public BSTPanel(RedBlackBST BST, int width, int height){
        setBackground(Color.WHITE);
        this.BST = BST;
        this.setSize(width, height);
        animator = new Timer(delay, this);

        //This is for non-repeating keys
        for(int i = 0; i<randomNums.length; i++){
            randomNums[i] = i;
        }
        Collections.shuffle(Arrays.asList(randomNums));
        BST.insert(randomNums[0]);


        treeHeight = getTreeHeight();
        treeWidth = getTreeWidth();
        animator.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintTree(g, BST.root, 1, 1);
    }

    //paints a node horizontally centered at x whose top is at y
    public void paintNode(Graphics g, RedBlackBST.Node Node, int x, int y){
        if(Node.isRed){
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawOval(x - nodeDiam/2, y, nodeDiam, nodeDiam);
        drawCenteredString(g, Integer.toString(Node.stuff), new Rectangle(x - nodeDiam/2, y, nodeDiam, nodeDiam), new Font("Courier", Font.PLAIN, nodeDiam/2));
    }

    //Recursively paint the tree with connecting lines
    public void paintTree(Graphics g, RedBlackBST.Node current, int level, int nodeNum){
        if(current == null){
            return;
        } else {
            int yIncrement;
            //special case to prevent dividng by zero
            if(treeHeight == 1){
                yIncrement = (getHeight() - 2 * nodeDiam);
            } else {
                yIncrement = (getHeight() - 2 * nodeDiam) / (treeHeight - 1);
            }
            int numOfNodes = (int) Math.pow(2, level - 1);
            int xIncrement = (getWidth() / (numOfNodes + 1));
            int x = xIncrement * nodeNum;
            int y = (level -1) * yIncrement;

            int nextNumOfNodes = (int) Math.pow(2, level);
            int nextXIncrement = (getWidth() / (nextNumOfNodes + 1));

            //paint current node
            paintNode(g, current, x, y);
            //paint left node
            paintTree(g, current.leftNode, level + 1, (nodeNum * 2) - 1);
            //paint connecting line to the left node
            if(current.leftNode!=null) {
                g.setColor(Color.BLACK);
                g.drawLine(x, y + nodeDiam, nextXIncrement * (nodeNum * 2 - 1), y + yIncrement);
            }
            //paint right node
            paintTree(g, current.rightNode, level + 1, nodeNum * 2);
            //paint connecting line to the right node
            if(current.rightNode!=null) {
                g.setColor(Color.BLACK);
                g.drawLine(x, y + nodeDiam, nextXIncrement * nodeNum * 2, y + yIncrement);
            }
        }
    }

    //This is triggered periodically by the animator
    public void actionPerformed(ActionEvent e) {
        //add a random non-repeating element to bst
        BST.insert(randomNums[index]);
        index++;
        treeHeight = getTreeHeight();
        treeWidth = getTreeWidth();
        //change diameter based on tree height to prevent overlapping nodes
        nodeDiam = 60  - (5 * (treeHeight-1));
        repaint();
    }

    //Source: https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }

    //Returns the VISUAL height of the tree for graphics purposes, treats reds and blacks as the same
    private int getTreeHeight(){
        return getTreeHeightHelper(BST.root, 0);
    }

    private int getTreeHeightHelper(RedBlackBST.Node node, int total){
        if(node == null){
            return total;
        }
        else {
            total ++;
            if(getTreeHeightHelper(node.leftNode, total) > getTreeHeightHelper(node.rightNode, total)){
                return getTreeHeightHelper(node.leftNode, total);
            } else {
                return getTreeHeightHelper(node.rightNode, total);
            }
        }
    }

    //returns the width of the tree for graphics purposes
    private int getTreeWidth(){
        int leftWidth = 0;
        int rightWidth = 0;

        RedBlackBST.Node leftCurr = BST.root;
        RedBlackBST.Node rightCurr = BST.root;

        while(leftCurr != null){
            leftCurr = leftCurr.leftNode;
            leftWidth++;
        }
        while(rightCurr != null){
            rightCurr = rightCurr.rightNode;
            rightWidth++;
        }

        //minus 1 for counting the root twice
        return leftWidth + rightWidth - 1;
    }
}

