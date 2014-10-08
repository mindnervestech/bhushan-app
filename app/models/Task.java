package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;

@Entity
public class Task extends Model{

	@Id
	public Long id;
	
	public String pname;
	public String category;
	
	public Task(String pname,String pageCategory){
		this.pname = pname;
		this.category = pageCategory;
	}
	
	
	//private Finder<Long, Task> find = new Finder<Long, Task>(Long.class, Task.class);
	
	public static Task saveTask(String pname,String pageCategory) {
		Task task = new Task(pname,pageCategory);
		task.save();
		return task;
	}
	
	
	public static List<Task> getPages(){
		List<Task> list = Ebean.find(Task.class).orderBy("pname asc").findList();
		return list;
	}
	
	public static List<String> getCategories(){
		SqlQuery query = Ebean.createSqlQuery("select distinct category from task");
		List<SqlRow> rows = query.findList();
		List<String> list = new ArrayList<>();
		for(SqlRow row : rows) {
			list.add(row.getString("category"));
		}
		return list;
	}
	
	public static List<Task> delTask(Long pid){
		Task dpage = Ebean.find(Task.class, pid);
		Ebean.delete(dpage);
		List<Task> list = Ebean.find(Task.class).findList();
		return list;
	}
	
	public static List<Task> searchTask(String sname,String category){
		List<Task> pageList = new ArrayList<>();
		if(!category.trim().equals("")) {
			pageList = Ebean.find(Task.class)
					       .where().and(Expr.like("pname","%"+sname+"%"), Expr.like("category","%"+category+"%"))
					       .findList();
		}
		else {
			pageList = Ebean.find(Task.class)
				       .where().like("pname","%"+sname+"%")
				       .findList();
		}
		return pageList;
	}
}
