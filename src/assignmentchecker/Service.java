/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentchecker;

import static assignmentchecker.FrontPageController.files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tesla
 */
public class Service {

    public List<Each> getComparison() {

        Parser p = new Parser();
        CosineSimilarity cs = new CosineSimilarity();

//		Each each = new Each();
        List<Each> eachList = new ArrayList<Each>();
        for (String firstFile : files) {
            String f1 = firstFile;
            C_File c_file1 = new C_File(f1);
            for (String secondFile : files) {
                if (firstFile.equals(secondFile)) {
                    continue;
                }
                String f2 = secondFile;

                Each each = new Each();

                C_File c_file2 = new C_File(f2);

//				Set the Each properties
                each.setFileName1(c_file1.getName());
                each.setFileName2(c_file2.getName());
                each.setPath1(f1);
                each.setPath2(f2);

               double percentage = (cs.CosineSimilarityScore(p.getCode(f1), p.getCode(f2))) * 100;
//                System.out.println(f1+"===="+f2+"-->"+ percentage);
                each.setPercentage(percentage);

                eachList.add(each);
                System.out.println(each);
            }
        }
        
        return eachList;

    }

}
