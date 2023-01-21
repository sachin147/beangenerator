import domain.Attribute;
import domain.Type;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Yaml yaml = new Yaml(new Constructor(Type.class));
        InputStream inputStream = Main.class
                .getClassLoader()
                .getResourceAsStream("bean.yaml");
        Type type = yaml.load(inputStream);
        System.out.println(type);

        StringBuilder code = new StringBuilder();
        String className = type.getName();
        code.append("public class ").append(className).append("{\n");
        for(Attribute attribute : type.getAttributes()) {
            code.append(attribute.getType()).append(" ").append(attribute.getName()).append(";\n");
        }

        code.append("\n");
        code.append("public "+className+"(");
        for(Attribute attribute : type.getAttributes()) {
            code.append(attribute.getType()).append(" ").append(attribute.getName()).append(",");
        }
        code.append(")\n");
        for(Attribute attribute : type.getAttributes()) {
            code.append(code.append("this.").append(attribute.getName()).append(" = ").append(attribute.getName()).append(";\n"));
        }
        code.append("}");
        System.out.println(code.toString());

        FileOutputStream fileOutputStream = new FileOutputStream( "Customer.java" );
        PrintWriter out = new PrintWriter( fileOutputStream );
        out.println(code.toString());
        out.flush();

        Process p = Runtime.getRuntime().exec( "javac Customer.java" );
        p.waitFor();

        if ( p.exitValue() == 0 ) {
            Class outputClassObject = Class.forName( "Customer" );
        } else {

        }

    }

}
