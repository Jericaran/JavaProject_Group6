package gitsimulator;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Scanner;

public class Test {
    //���ɵ����ļ��л��ļ���hashֵ���Ժ���
    public static void testSingleKey(String filepath){
        File file = new File(filepath);//����·������file�ļ�
        try{
            if(file.isDirectory()){//�ļ�������
                Tree tree = new Tree(file);//����Tree����hashֵ
                tree.write();//��KeyValueд��objects�洢�ļ���
                System.out.println("�ļ��е�hashֵΪ��" + tree.GetKey());
            }
            else if(file.isFile()){
                Blob blob = new Blob(file);//����Blob����hashֵ
                blob.write(filepath);//��KeyValueд��objects�洢�ļ���
                System.out.println("�ļ���hashֵΪ��" + blob.GetKey());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //���������ļ���project��hashֵ���Ժ���,Ŀǰֻ�ܲ����ļ��У����Ե����ļ����ܻᱨ��
    public static void testAllKey(String filepath){
        testSingleKey(filepath);//��Ҫ���ȶ��ļ���·������hashֵ����
        File dir = new File(filepath);
        File[] fs = dir.listFiles();
        int a = fs.length;
        for(int i = 0; i < a; i++) {
            if(fs[i].isFile()) {//��ȡ���ļ�����
                try{
                    Blob blob = new Blob(filepath + File.separator + fs[i].getName());
                    blob.write(filepath + File.separator + fs[i].getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(fs[i].isDirectory()) {//��ȡ���ļ�������
                try{
                    Tree tree = new Tree(filepath + File.separator + fs[i].getName());
                    tree.write();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                testAllKey(filepath + File.separator + fs[i].getName());//�ݹ���ò����ļ��к���
            }
        }
    }
    //����Commit���Ժ���
    public static String testCommit(String filepath) throws Exception {
        String author = "java";//commit���ߣ��ɽ���
        String committer = "java";//commit�ύ�ߣ��ɽ���
        String comment = "success!";//commit���ۣ��ɽ���
        String parent = "null";//��ʼ�趨��һ��commit��ǰһ��commit������
        Commit commit = new Commit(filepath, parent, author, committer, comment);//����commit
        commit.ComputeCommit();//����commit��hashֵ
        commit.write();//��KeyValueд��objects�洢�ļ���
        System.out.println("�ɹ��ύcommit: " + commit.GetKey());//��ʾ�ɹ��ύcommit����ʾcommit��hashֵ
        System.out.println(commit.ShowCommitList());//��ʾ��ǰcommit�б�
        return commit.GetKey();//����commit��hashֵ
    }
    //����reset���Ժ���
    public static void testReset(String commitkey, String resetpath) throws Exception {
        Reset reset = new Reset();//����reset
        reset.Resetcommit(commitkey, resetpath);//������Ҫreset��commitֵ�Լ��ļ�������Ҫ�ָ����Ĺ���·������reset
    }

    public static void main(String args[]) throws Exception {
        HEAD head = new HEAD();//���ɳ�ʼ��HEAD�ļ�
        Branch master = new Branch("master","");//���ɳ�ʼ��master����֧�����ݳ�ʼ��Ϊ��
        Scanner input = new Scanner(System.in);
        System.out.println("�������ļ����ļ��е�·������");
        String path = input.next();
        testAllKey(path);//������·������KeyValue�洢��������commit֮ǰ�Ƚ���KeyValue�洢
        String commitkey = testCommit(path);//�����ݸ�·�����ɵ�һ��commit
        master.updateBranch(commitkey);//��������֧master������
        testSingleKey("D:\\Java\\test.docx");//���Ե����ļ���hashֵ����
        commitkey = testCommit("D:\\Java\\test.docx");//�������ļ���Ϊcommit���ύ��ʽ�ύ
        master.updateBranch(commitkey);//��������֧master������
        Branch branch = new Branch("branch", master.getCommitId());//����һ���µķ�֧
        head.updateHEAD("branch");//�л�������֧������HEADָ���µķ�֧
        testAllKey("D:\\Java\\homework1105");//����һ���µĹ���·��
        commitkey = testCommit("D:\\Java\\homework1105");//����Ϊcommit�ύ
        branch.updateBranch(commitkey);//���·�֧������
        testReset("7a2035435f15bcf795194defb13f6b18e339a1", "D:\\Java\\reset");//����commit��hashֵ������reset����
    }
}
//���Բ���˵����KeyValue�Ĳ���������Ϊ���ύcommit���ڲ���commit��ʱ�򣬲�����������KeyValue�����������޷�reset�����⣬Ŀǰreset�����ָܻ������ļ��ύ��commit
