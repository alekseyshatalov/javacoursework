package com.javacourse.coursework.adapters;

import com.javacourse.coursework.Logger;
import com.javacourse.coursework.models.Speciality;
import com.javacourse.coursework.models.SpecialtyDAO;
import com.javacourse.coursework.models.StudentDAO;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.rmi.runtime.Log;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ImportAdapter {

    public static void importData(String filePath, String fileName) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filePath, fileName));

            NodeList data = document.getDocumentElement().getChildNodes();

            NodeList specialities = data.item(1).getChildNodes();
            NodeList students = data.item(3).getChildNodes();

            for (int i = 0; i < specialities.getLength(); i++) {
                if(specialities.item(i).getNodeType() != Node.TEXT_NODE) {
                    NodeList speciality = specialities.item(i).getChildNodes();
                    int id = Integer.parseInt(speciality.item(1).getTextContent());
                    String name = speciality.item(3).getTextContent();
                    String description = speciality.item(5).getTextContent();

                    SpecialtyDAO.create(id, name, description);
                }
            }

            for (int i = 0; i < students.getLength(); i++) {
                if(students.item(i).getNodeType() != Node.TEXT_NODE) {
                    NodeList student = students.item(i).getChildNodes();
                    int id = Integer.parseInt(student.item(1).getTextContent());
                    String name = student.item(3).getTextContent();
                    Speciality speciality = SpecialtyDAO.get(Integer.parseInt(student.item(5).getTextContent()));
                    float mark = Float.parseFloat(student.item(7).getTextContent());
                    StudentDAO.create(id, name, speciality, mark);
                }
            }

            Logger.write("import completed");

        } catch (ParserConfigurationException | IOException | SAXException e) {
            Logger.write(e.getMessage());
        }
    }

}
