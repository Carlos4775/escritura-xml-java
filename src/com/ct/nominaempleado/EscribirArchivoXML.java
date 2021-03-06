package com.ct.nominaempleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class EscribirArchivoXML {
    
    public static void main(String[] args) {
              
        Connection connection = null;
        Statement insertStmt = null;
        Statement selectStmt = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nomina_empleado", "root", "");
            selectStmt = connection.createStatement();
            ResultSet rs = selectStmt.executeQuery("SELECT * from empleado");
            
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                DOMImplementation implementation = builder.getDOMImplementation();

                Document documento = implementation.createDocument(null, "nomina", null);
                documento.setXmlVersion("1.0");

                Element coches = documento.createElement("empleados");

                while(rs.next()){
                    Element coche = documento.createElement("empleado");

                // Matricula
                Element matricula = documento.createElement("cedula");
                Text textMatricula = documento.createTextNode(rs.getString(1));
                matricula.appendChild(textMatricula);
                coche.appendChild(matricula);

                // Marca
                Element marca = documento.createElement("numeroCuenta");
                Text textMarca = documento.createTextNode(rs.getString(2));
                marca.appendChild(textMarca);
                coche.appendChild(marca);

                // Precio
                Element precio = documento.createElement("salario");
                Text textPrecio = documento.createTextNode(rs.getString(3));
                precio.appendChild(textPrecio);
                coche.appendChild(precio);
                // Añado al elemento coches el elemento coche
                coches.appendChild(coche);
                }
                // Añado al root el elemento coches
                documento.getDocumentElement().appendChild(coches);

                // Asocio el source con el Document
                Source source = new DOMSource(documento);
                // Creo el Result, indicado que fichero se va a crear
                Result result = new StreamResult(new File("empleados.xml"));

                // Creo un transformer, se crea el fichero XML
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, result);

            } catch (ParserConfigurationException | TransformerException ex) {
                System.out.println(ex.getMessage());
            }            
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
