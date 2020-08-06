package com.by;

import java.util.LinkedList;


/**
 * @author by
 * @description:
 * @date 2020/7/25 11:07
 */

public class AVLTree {
    private AVLNode root;

    public AVLTree(int data) {
        this.root = new AVLNode(data);
    }

    public AVLTree() {
    }

    public boolean put(int data) {
        if (root == null) {
            root = new AVLNode(data);
            return true;
        }
        AVLNode node = getNode(data);
        if (node.data == data || node.left != null && node.left.data == data
                || node.right != null && node.right.data == data)
            return false;
        if (node.data < data)
            node.right = new AVLNode(data, node);
        else if (node.data > data)
            node.left = new AVLNode(data, node);
        selfBalance(node.parent);
        return true;
    }

    public boolean remove(int data) {
        if (root == null)
            return false;
        AVLNode node = getNode(data);
        if (node.data != data)
            return false;
        AVLNode parent = node.parent;
        if (node.left != null && node.right != null) {
            AVLNode replaceNode = getReplaceNode(node.left);
            node.data = replaceNode.data;
            replaceNode.parent.right = replaceNode.left;
            if (replaceNode.left != null)
                replaceNode.left.parent = replaceNode.parent;
            parent = replaceNode.parent;
        } else if (node.left == null) {
            if (parent == null) {
                node.right.parent = null;
                root = node.right;
            } else {
                if (parent.data > data) {
                    parent.left = node.right;
                    node.right.parent = parent;
                } else {
                    parent.right = node.right;
                    node.right.parent = parent;
                }
            }
        } else if (node.right == null) {
            if (parent == null) {
                node.left.parent = null;
                root = node.left;
            } else {
                if (parent.data > data) {
                    parent.left = node.left;
                    node.left.parent = parent;
                } else {
                    parent.right = node.left;
                    node.left.parent = parent;
                }
            }
        }
        selfBalance(parent);
        return true;
    }

    public boolean contains(int data) {
        AVLNode node = root;
        while (node != null) {
            if (node.data == data)
                return true;
            else if (node.data > data)
                node = node.left;
            else
                node = node.right;
        }
        return false;
    }

    //四大遍历
    public String preorder() {
        StringBuilder sb = new StringBuilder();
        preorder(sb, root);
        return sb.toString();
    }

    public String inorder() {
        StringBuilder sb = new StringBuilder();
        inorder(sb, root);
        return sb.toString();
    }

    public String postorder() {
        StringBuilder sb = new StringBuilder();
        postorder(sb, root);
        return sb.toString();
    }

    public String levelorder() {
        StringBuilder sb = new StringBuilder();
        levelorder(sb, root);
        return sb.toString();
    }

    //先序遍历
    private void preorder(StringBuilder sb, AVLNode node) {
        if (node != null)
            sb.append(node.data + " ");
        if (node.left != null)
            preorder(sb, node.left);
        if (node.right != null)
            preorder(sb, node.right);
    }

    //中序遍历
    private void inorder(StringBuilder sb, AVLNode node) {
        if (node.left != null)
            inorder(sb, node.left);
        if (node != null)
            sb.append(node.data + " ");
        if (node.right != null)
            inorder(sb, node.right);
    }

    //后序遍历
    private void postorder(StringBuilder sb, AVLNode node) {
        if (node.left != null)
            postorder(sb, node.left);
        if (node.right != null)
            postorder(sb, node.right);
        if (node != null)
            sb.append(node.data + " ");
    }

    //层序遍历
    private void levelorder(StringBuilder sb, AVLNode node) {
        if (node == null)
            return;
        LinkedList<AVLNode> list = new LinkedList<>();
        list.add(node);
        while (!list.isEmpty()) {
            node = list.poll();
            sb.append(node.data + " ");
            if (node.left != null)
                list.offer(node.left);
            if (node.right != null)
                list.offer(node.right);
        }
    }

    //获取最接近data的结点
    private AVLNode getNode(int data) {
        AVLNode node = root;
        while (node != null) {
            if (node.data == data)
                return node;
            else if (node.data > data) {
                if (node.left != null)
                    node = node.left;
                else
                    return node;
            } else if (node.right != null)
                node = node.right;
            else
                return node;
        }
        return null;
    }

    //获得左子树最右侧（值最大）结点
    private AVLNode getReplaceNode(AVLNode node) {
        while (node.right != null)
            node = node.right;
        return node;
    }

    //左旋，逆时针旋转
    private void leftRotate(AVLNode node) {
        AVLNode parent = node.parent;
        AVLNode leftSon = node.left;
        node.left = parent;
        node.parent = parent.parent;
        parent.right = leftSon;
        parent.parent = node;
        if (node.parent != null)
            node.parent.right = node;
        if (leftSon != null)
            leftSon.parent = parent;
        if (node.parent == null)
            root = node;
    }

    //右旋，顺时针旋转
    private void rightRotate(AVLNode node) {
        AVLNode parent = node.parent;
        AVLNode rightSon = node.right;
        node.right = parent;
        node.parent = parent.parent;
        parent.left = rightSon;
        parent.parent = node;
        if (node.parent != null)
            node.parent.left = node;
        if (rightSon != null)
            rightSon.parent = parent;
        if (node.parent == null)
            root = node;
    }

    //自我平衡
    private void selfBalance(AVLNode node) {
        while (node != null) {
            node.balance = calcBalance(node);
            //右子树多
            if (node.balance >= 2) {
                if (node.right.right != null)
                    leftRotate(node.right);
                else {
                    rightRotate(node.right.left);
                    leftRotate(node.right);
                }
            }
            //左子树多
            else if (node.balance <= -2) {
                System.out.println(1);
                if (node.right.right != null)
                    leftRotate(node.left);
                else {
                    leftRotate(node.left.right);
                    rightRotate(node.left);
                }
            }
            node = node.parent;
        }
    }

    //左右子树高度之差
    private int calcBalance(AVLNode node) {
        if (node == null)
            return 0;
        return calcNum(node.right) - calcNum(node.left);
    }

    //递归计算当前结点的高度
    private int calcNum(AVLNode node) {
        if (node == null)
            return 0;
        int leftNum = 0, rightNum = 0;
        if (node.left != null)
            leftNum = calcNum(node.left);
        if (node.right != null)
            rightNum = calcNum(node.right);
        return 1 + (leftNum > rightNum ? leftNum : rightNum);
    }

    private class AVLNode {
        int data;

        int balance;

        AVLNode left;

        AVLNode right;

        AVLNode parent;

        AVLNode(int data) {
            this.data = data;
            this.balance = 0;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        AVLNode(int data, AVLNode parent) {
            this.data = data;
            this.balance = 0;
            this.left = null;
            this.right = null;
            this.parent = parent;
        }
    }
}
