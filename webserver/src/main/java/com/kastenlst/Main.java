package com.kastenlst;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Main {
    public static void main(String[] args) {
    }
    public static Document paraseXML(File file) throws Exception {
        Document result = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        result.getDocumentElement().normalize();
        return result;
    }
}
