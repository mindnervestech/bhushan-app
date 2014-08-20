package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;

import play.db.ebean.Model;

@Entity
public class Task extends Model{

	@Id
	public Long id;
	
	public String pname;
	
	public Task(String pname){
		this.pname = pname;
	}
	
	
	private Finder<Long, Task> find = new Finder<Long, Task>(Long.class, Task.class);
	
	public static Task saveTask(String pname) {
		Task task = new Task(pname);
		task.save();
		return task;
	}
	
	
	public static List<Task> getPages(){
		List<Task> list = Ebean.find(Task.class).findList();
		return list;
	}
	
	public static List<Task> delTask(Long pid){
		Task dpage = Ebean.find(Task.class, pid);
		Ebean.delete(dpage);
		List<Task> list = Ebean.find(Task.class).findList();
		return list;
	}
	
	public static List<Task> searchTask(String sname){
		List<Task> pageList = Ebean.find(Task.class)
				       .where()
				       .like("pname","%"+sname+"%")
				       .findList();
		return pageList;
	}
}
