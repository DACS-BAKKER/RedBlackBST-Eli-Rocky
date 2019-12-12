public class RedBlackBST {

    public Node root;

    public RedBlackBST() {
        root = null;
    }

    //Wrapper method for insertion
    //Always set the root to a black node to prevent illegal trees
    public void insert(int number){
        root = insertHelper(number, root);
        root.isRed = false;
    }

    //Slight modification on binary search tree insertion
    //Pointers are organized and then returned after each recursion
    public Node insertHelper(int number, Node node){
        if(node == null){
            return new Node(number);
        }
        else {
            if(number < node.stuff){
                node.leftNode = insertHelper(number, node.leftNode);
            }
            else if(number > node.stuff){
                node.rightNode = insertHelper(number, node.rightNode);
            }
        }
        return organizeTree(node);
    }

    //It's always the null pointers messing things up smh
    //I have if statements for days
    public Node organizeTree(Node origin){
        if(origin.rightNode != null){
            if(origin.rightNode.isRed == true && (origin.leftNode == null || origin.leftNode.isRed == false)){
                origin = leftRotation(origin);
                switchColors(origin, origin.leftNode);
            }
        }
        if(origin.leftNode != null){
            if(origin.leftNode.leftNode != null){
                if(origin.leftNode.isRed == true && origin.leftNode.leftNode.isRed == true){
                    origin = rightRotation(origin);
                    switchColors(origin, origin.rightNode);
                }
            }
        }
        if(origin.leftNode != null){
            if(origin.rightNode != null){
                if(origin.leftNode.isRed == true && origin.rightNode.isRed == true){
                    origin.isRed = !origin.isRed;
                    origin.leftNode.isRed = false;
                    origin.rightNode.isRed = false;
                }
            }
        }
        return origin;
    }

    //Left rotation method
    public Node leftRotation(Node origin){
        Node newOrigin = origin.rightNode;
        origin.rightNode = newOrigin.leftNode;
        newOrigin.leftNode = origin;
        return newOrigin;
    }

    //Right rotation method
    public Node rightRotation(Node origin){
        Node newOrigin = origin.leftNode;
        origin.leftNode = newOrigin.rightNode;
        newOrigin.rightNode = origin;
        return newOrigin;
    }

    //Switching the colors of two nodes
    public void switchColors(Node firstNode, Node secondNode){
        boolean firstNodeColor = firstNode.isRed;
        firstNode.isRed = secondNode.isRed;
        secondNode.isRed = firstNodeColor;
    }

    public void printPreOrder(){
        preOrder(root);
    }
    private void preOrder(Node node){
        if(node != null){
            System.out.println(node.stuff + " ");
            preOrder(node.leftNode);
            preOrder(node.rightNode);
        }
    }
    public static void main(String[] args) {
        RedBlackBST myBST = new RedBlackBST();
        myBST.insert(19);
        myBST.insert(2);
        System.out.println(myBST.root.leftNode.stuff);
    }

    public static class Node {
        public int stuff;
        public Node leftNode;
        public Node rightNode;
        public boolean isRed;

        Node(int stuff){
            this.stuff = stuff;
            this.leftNode = null;
            this.rightNode = null;
            this.isRed = true;
        }
    }
}
