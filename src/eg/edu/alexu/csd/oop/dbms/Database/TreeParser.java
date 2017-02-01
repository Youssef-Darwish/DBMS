package eg.edu.alexu.csd.oop.dbms.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.dbms.query.AlterQuery;
import eg.edu.alexu.csd.oop.dbms.query.CreateQuery;
import eg.edu.alexu.csd.oop.dbms.query.DeleteRow;
import eg.edu.alexu.csd.oop.dbms.query.DistinctQuery;
import eg.edu.alexu.csd.oop.dbms.query.IQuery;
import eg.edu.alexu.csd.oop.dbms.query.Insert;
import eg.edu.alexu.csd.oop.dbms.query.SelectQuery;
import eg.edu.alexu.csd.oop.dbms.query.Update;

public class TreeParser implements ISQLParser {


  private HashMap<String, String> patterns;
  private HashMap <String,String[]> composition;
  private ArrayList<String> sensitive;
  public HashMap<String,Object> catches;
  private String oriQuery;
  @Override
  public IQuery parse(String query) throws Exception {
    catches  = new HashMap<String,Object>();
    query = query.trim();
    oriQuery = query;
    query = query.toLowerCase();
    int tt=query.lastIndexOf("where");
    if(tt==-1)
      tt=query.length();
    String cond = query.substring(tt, query.length());
    query = query.substring(0,tt);
    
    
    String type = query.split(" ")[0];
    if(type.equals("alter") && query.contains("add"))
      type = "add";
    else if(type.equals("alter") && query.contains("drop"))
      type = "drop";
    else if(type.equals("select"))  {
      catches.put("distinct", new Boolean(query.contains("distinct")));
      query = query.replace("distinct","");
    }
    if (   !bfvalidate(type,query)||!bfvalidate("where",cond) )
      throw new RuntimeException("not a correct sql query");
    return querfyfacto(type);
  }
  public TreeParser() {
    patterns = new HashMap<String, String>();
    composition = new HashMap <String,String[]> ();
    catches  = new HashMap<String,Object>();
    sensitive = new ArrayList<String>();
    sensitive.add("sval");
    sensitive.add("sset");
    loadPatterns();
    loadCompositon();
  }
  public void loadPatterns() {

    patterns.put("select", "select\\s*(.*)\\s+(from \\S+)\\s*");
    patterns.put("distinct", "select\\s+distinct\\s+(.*)\\s+(from \\S+)\\s*");
    patterns.put("insertw", "insert\\s+(into+\\s*+[a-z_0-9]+)\\s*(.*)\\s+values+\\s*(.*)");
    patterns.put("insert", "insert\\s+(into+\\s*+[a-z_0-9]+)+\\s+values+\\s*(.*)");
    
    patterns.put("update", "(update\\s*+\\S+)\\s+set\\s*(.*)\\s*");
    patterns.put("create", "create\\s+(table\\s+[a-z_0-9]+)\\s*(.*)");
    patterns.put("delete", "delete\\s*+(from\\s+\\S+)\\s*");
    patterns.put("where", "where\\s*(\\S+)\\s*(>|<|=)\\s*(\\S+)");
    patterns.put("table", "[from|update|into|table]+\\s*+([a-z_][a-z@0-9#_]+)");
    patterns.put("drop", "alter\\s+(table\\s+\\S+)\\s+drop\\s+column?\\s+(\\S+)\\s*");
    patterns.put("add", "alter\\s+(table\\s+\\S+)\\s+add\\s+column\\s+(\\S+)\\s+(\\S+)\\s*");

    patterns.put("addw", "alter\\s+(table\\s+\\S+)\\s+add\\s+(\\S+)\\s+(\\S+)\\s*");
//    patterns.put("sets", "set+\\s*+(.*)");
//    patterns.put("vals", "values+\\s*+(.*)");
//    
    patterns.put("sval", "(.+)");
    patterns.put("sinit", "\\s*([a-z_][a-z@0-9#_]+)\\s+(varchar|int|date|float)\\s*");

    patterns.put("sset", "([a-z_][a-z@0-9#_]+)=(.+)");
    patterns.put("scol", "([a-z_][a-z@0-9#_]+|\\*)");
    
    patterns.put("oprtr", "(>|<|=)");
     
  }
  public void loadCompositon() {
    composition.put("select", new String[]{"cols","table"});
    composition.put("insertw", new String[]{"table","cols","vals"});
    composition.put("insert", new String[]{"table","vals"});
    composition.put("update", new String[]{"table","sets"});
    composition.put("create", new String[]{"table","inits"});
    composition.put("delete", new String[]{"table"});
    composition.put("where", new String[]{"scol","oprtr","sval"});
    composition.put("add", new String[]{"table","scol","sval"});

    composition.put("addw", new String[]{"table","scol","sval"});

    composition.put("drop", new String[]{"table","scol"});
    
  }
  public boolean validate(String type, String query) {
    if(query==null || !patterns.containsKey(type))
      return false;
    Pattern verify = Pattern.compile(patterns.get(type));
    Matcher match = verify.matcher(query.toLowerCase());
   
    //System.out.println("hertrue");
    if (!query.matches(patterns.get(type))) {
      //System.out.println(patterns.get(type));
      
      return false;
    }


    return true;
  }
  public boolean bfvalidate(String query , String input) {
    query = query.trim();
    input = input.trim();
    if ( (input == null||input.length()==0) && (query.equals("where") || query.equals("cols")))
      return true;
    
    System.out.println(query+" "+input+" ");
    if(query.endsWith("s")) {
      return groupvalidate(query,input);
    }

    if(!validate(query,input))
      {
        if(query.equals("insert") || query.equals("add") )
          return bfvalidate(query+"w",input);
        return false;
      
      }

  
    for (int i=0;composition.containsKey(query) &&
        i<extractattr(query,input).size();i++) {
      if(extractattr(query,input)!=null &&
          !bfvalidate(composition.get(query)[i],
          extractattr(query,input).get(i)))
        return false;
      
    }
    catches.put(query, extractattr(query,input).get(0));
    return true;
  }
  public List<String> extractattr(String type, String query) {
    Pattern verify = Pattern.compile(patterns.get(type));
    Matcher match = verify.matcher(query.toLowerCase());
    if (!match.matches()) {
      return null;
    }
    List<String> toretur = new ArrayList<String>();
    for (int i = 1; i <= match.groupCount(); i++) {
      if(match.group(i)!=null)
      toretur.add(match.group(i).trim());
    }

    return toretur;
  }
  public boolean groupvalidate(String query,String input) {
    input = input.replace('(', ' ').replace(')', ' ');
    input = input.trim();
    String pquery = "s"+query.substring(0,query.length()-1);
    String[] inputs = input.split(",");
    for (int i = 0;i<inputs.length ; i++) {
      System.out.println(pquery + " "+ inputs[i] + " g");
      inputs[i] = inputs[i].trim();
      if (!validate(pquery,inputs[i])) {
        return false;
      }
    }
    Object zis;

    if(extractattr(pquery,inputs[0]).size() == 1) {
      zis = new ArrayList<String>();
      for (int i = 0;i<inputs.length ; i++) {
          ((List<String>)zis).add(extractattr(pquery,inputs[i]).get(0));
        }
    }
    else {
      List<String> ats = new ArrayList<String>();
      zis = new HashMap<String,Object>();
      for (int i = 0;i<inputs.length ; i++) {//System.out.println(query+ " "+extractattr(pquery,inputs[i]));
          ((HashMap<String,String>)zis).put(extractattr(pquery,inputs[i]).get(0).trim(),
              extractattr(pquery,inputs[i]).get(1).trim());     
        ats.add(extractattr(pquery,inputs[i]).get(0));
      }
      catches.put(query+"1", ats);
    }
      
    catches.put(query, zis);
    return true;
  }
  public IQuery querfyfacto(String str) {
    IQuery retval;
    makecond();
    if(str.equals("select")){
      if ((Boolean)catches.get("distinct") == false)
        retval =  new SelectQuery();
      else
        retval =  new DistinctQuery();
      retval.setTable((String)catches.get("table"));
      HashMap<String,Object> temp = new HashMap<String,Object>(); 
      List<String> keys =(List<String>)catches.get("cols");
      for ( String key : keys) {
        temp.put(key,null);
      }
      retval.setAttributes(temp);
      ((SelectQuery)retval).setAttributes(keys);
      retval.setCondition((Condition)catches.get("cond")); 
      
    }

    else if(str.equals("update")){
      retval =  new Update();
      retval.setTable((String)catches.get("table"));
      HashMap<String,Object> temp = ((HashMap<String,Object>) catches.get("sets"));
      retval.setAttributes(temp);
      retval.setCondition((Condition)catches.get("cond"));    
    }

    else if(str.equals("delete")){
      retval =  new DeleteRow();
      retval.setTable((String)catches.get("table"));
      retval.setCondition((Condition)catches.get("cond"));    
    }

    else if(str.equals("insert")){
      retval =  new Insert();
      retval.setTable((String)catches.get("table"));
       HashMap<String,Object> temp = new HashMap<String,Object>();
     if(catches.containsKey("cols") && 
         !((List<String>) catches.get("cols")).get(0).equals("*")) { 
       List<String> keys = (List<String>) catches.get("cols");
       List<String> vals = (List<String>) catches.get("vals");
       if(vals.size() != keys.size())
         throw new RuntimeException("wrong format");
       for (int i = 0 ; i<keys.size();i++ ) {
         temp.put(keys.get(i), vals.get(i));
       }
      retval.setAttributes(temp);
     }
     else
     {
       ((Insert)retval).setAttributes((List<String>)catches.get("vals"));
     }
      retval.setCondition((Condition)catches.get("cond"));    
    }
    
    else if(str.equals("create")){
     // System.out.println((String)catches.get("table"));
      retval = new CreateQuery();
      retval.setTable((String)catches.get("table"));
      HashMap<String,Object> temp = ((HashMap<String,Object>) catches.get("inits"));
     // System.out.println(temp);
      
      retval.setAttributes(temp); 
      ((CreateQuery) retval).setAttributes((List<String>)catches.get("inits1"));
    } else if(str.equals("add")) {
      AlterQuery alt =new AlterQuery();
      alt.setTable((String)catches.get("table"));
      HashMap <String  , Object> newcol = new HashMap <String  , Object>();
      newcol.put((String)catches.get("scol"), catches.get("sval"));
      alt.addOrDrop(1);
      alt.setAttributes(newcol);
      retval = alt;
      
    } else if (str.equals("drop")) {
      AlterQuery alt =new AlterQuery();
      alt.setTable((String)catches.get("table"));
      HashMap <String  , Object> newcol = new HashMap <String  , Object>();
      newcol.put((String)catches.get("scol"), catches.get("sval"));
      alt.addOrDrop(0);
      alt.setAttributes(newcol);
      retval = alt;
    }
    else
      return null;
    return retval;
  }
  
  public void makecond() {
    if(!catches.containsKey("where") ||catches.get("where") ==null ) {
     
      catches.put("cond", new Condition());
    return;
    }
    if (catches.get("oprtr").equals(">"))
      catches.put("cond", new Greater((String)catches.get("scol"),((String)catches.get("sval")).replaceAll("'", "") ) );
    else if (catches.get("oprtr").equals("<"))
      catches.put("cond", new Smaller((String)catches.get("scol"),((String)catches.get("sval")).replaceAll("'", "") ) );

    else if (catches.get("oprtr").equals("="))
      catches.put("cond", new Equivalence((String)catches.get("scol"),((String)catches.get("sval")).replaceAll("'", "") ) );
  }
  public static void main(String[] arg) {
//  String query  = new SqlParser().removeInBetweenSpaces("update animals set (frog=f,cat=c,dog=c) where dogs>19;");
//  String[] splitted;
//  splitted = query.trim().toLowerCase().replace("(", "").replace(")", "").replace(";", "").split(" ");//regex
//  for(int i = 0;i<splitted.length;i++)//System.out.println(splitted[i]);   
//  System.out.println(new SqlParser().parse("insert into animal values 12,34,78;"));
//  System.out.println(new SqlParser().parse("select dogs from animals where cats = 'mice';"));
//  System.out.println(new SqlParser().parse("insert into animal values 12,34,78;"));
//  System.out.println(new SqlParser().parse("insert into animal values 12,34,78;"));
//  System.out.println(new SqlParser().parse("insert into animal values 12,34,78;"));
  String query = 
           //" create table table_name1(column_name1 varchar , column_name2 int, column_name3 varchar)";
    "alter table table_name13 add column_name4 int"
  //"select * from table_name13" 
      ;
  String temp = "hammo is here where he belongs";
  
  //query= query.toLowerCase();
   ISQLParser j  = new TreeParser();
 try {
    IQuery q =j.parse(query.toLowerCase());

    System.out.println(q.getTableName() + " "+q.getClass());
  } catch (Exception e) {
    System.out.println("O.o" + e.getMessage());
  }
//  System.out.println(j.bfvalidate("select","select alls from here where dog = 'cat';"));
//  System.out.println(j.catches.get("sval"));
  //System.out.println(new SqlParse().groupvalidate("scol","dogs,cats,mice"));
  
  ////System.out.println(new SqlParser().getvals(new SqlParser().extractattr("values", "values ( 10,12,80 )").get(0)));

}

}
