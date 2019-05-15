package com.qabbs.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineText;
            while ((lineText = bufferedReader.readLine()) != null) {
                addWord(lineText.trim());
            }
            read.close();
        } catch (Exception e) {
            System.out.println("读取敏感词失败");
        }
    }

    //增加关键词
    private void addWord(String lineText) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineText.length(); ++i) {
            Character c = lineText.charAt(i);
            if(isSymbol(c)) {
                continue;
            }

            TrieNode node = tempNode.getSubNode(c);

            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            tempNode = node;

            if (i == lineText.length() - 1) {
                tempNode.setkeywordEnd(true);
            }
        }
    }

    private class TrieNode {
        //是不是关键词结尾
        private boolean end = false;

        //当前节点下所有的子节点
        private Map<Character, TrieNode> subNodes = new HashMap<Character, TrieNode>();

        public void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isKeyworkEnd() {
            return end;
        }
        void setkeywordEnd(boolean end) {
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();

    //判断字符串有没有加别的字符
    private boolean isSymbol(char c) {
        int ic = (int) c;
        //东亚文字 0x2E80-0x9FFF
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filter(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }

        StringBuilder result = new StringBuilder();

        StringBuilder replacement = new StringBuilder("*");
        int count = 0;
        TrieNode tempNode = rootNode;//根节点
        int begin = 0;//前指针
        int position = 0;//判断指针

        while (position < text.length()) {
            char c = text.charAt(position);

            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }

                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            if (tempNode == null) {
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isKeyworkEnd()) {
                // 发现敏感词
                while(count > 0) {
                    replacement.append(replacement);
                    count--;
                }
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
                ++count;
            }
        }

        result.append(text.substring(begin));
        return result.toString();
    }

    public static void main(String[] args) {
        SensitiveService s =new SensitiveService();
        s.addWord("色情");
        s.addWord("老母");
        s.addWord("操");
        s.addWord("叼");
        System.out.println(s.filter("我叼你老 母"));
    }
}

