package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.function.Function;
import java.time.LocalDate;
import java.time.Month;
import org.junit.Assert;

public class TaskManagementSystem {
    public static ArrayList<Task> tasks;

    public static void init() {
        tasks = new ArrayList<>();
    }


    /* * * * * * * * * * * * * * * *
     * Operation on the task list *
     * * * * * * * * * * * * * * * */

    /**
     * TODO: add a new task object to the member variable *tasks*
     *
     * @param t: the task object to be added
     */
    public static void appendNewTask(Task t) {
    	tasks.add(t);
    }

    /**
     * TODO: find all unique tags from all the tasks
     * Note that each task could have multiple tags
     * If a tag occurs multiple times in the tasks, it should only occur once in the returned list.
     * All the tags should be sorted according to the order of the string content
     *
     * @return the sorted list of the tags, in which each tag only occurs once
     */
    public static List<String> findAndSortAllUniqueTags() {
        List<String> uniqueTags = new ArrayList<String>();
    	for (Task t : tasks) {
        	for (String tag : t.getTags()) {
        		if (!(uniqueTags.contains(tag))) {
        			uniqueTags.add(tag);
        		}
        	}
        }
    	
    	Collections.sort(uniqueTags);
    	
    	return uniqueTags;
    }

    /**
     * TODO: find the tasks satisfying the condition p and sort by the ascending order of the ID.
     * You have to define the methods returning various predicates over task objects
     *
     * @param p: the condition
     * @return the sorted list of the tasks. The sorting order is the ascending order of the ID.
     */
    public static List<Task> findTasks(Predicate<Task> p) {
    	List<Task> results = new ArrayList<Task>();
    	for (Task t : tasks) {
    		if (p.test(t)) {
    			results.add(t);
    		}
    	}
    	Collections.sort(results);
    	
    	return results;
    }

    /**
     * TODO: count the number of the tasks satisfying the condition p
     * You have to define the methods returning various predicates over task objects
     *
     * @param p: the condition
     * @return the number of the tasks satisfying p
     */
    public static int countTasks(Predicate<Task> p) {
    	int result = 0;
    	for (Task t : tasks) {
    		if (p.test(t)) {
    			result += 1;
    		}
    	}
    	
        return result;
    }

    /**
     * TODO: find the top-N tasks satisfying the condition p, which are sorted according to the ascending order of the ID.
     * You have to define the methods returning various predicates over task objects
     * If N > countTasks(p).size(), just return findTasks(p) directly.
     *
     * @param p: the condition
     * @return the sorted list of the tasks.
     */
    public static List<Task> getTopNTasks(Predicate<Task> p, int N) {
    	List<Task> correctTasks = findTasks(p);
    	
    	if (N > countTasks(p)) {		// Double check against findTasks(p).size()
    		return correctTasks;
    	}
    	
    	List<Task> results = new ArrayList<Task>();
    	
    	for (int i = 0; i < N; i++) {
    		results.add(correctTasks.get(i));
    	}
    	
        return results;
    }

    /**
     * TODO: remove the tasks satisfying the condition p from *tasks*
     *
     * @param p: the condition
     * @return: true if at least one task is removed, and otherwise return false.
     */
    public static boolean removeTask(Predicate<Task> p) {
    	int removedCount = 0;
    	Iterator<Task> itr = tasks.iterator();
    	while (itr.hasNext()) {
    		if (p.test(itr.next())) {
    			itr.remove();
    			removedCount++;
    		}
    	}
    	return (removedCount > 0) ? true : false; // You may also change this line
    }

    /* * * * * * * * * * * * * * * *
     * Predicate Definition *
     * * * * * * * * * * * * * * * */
    public static Predicate<Task> byType(TaskType type) {
    	return t -> t.getType() == type;
    }

    public static Predicate<Task> byTag(String tag) {
        return t -> t.getTags().contains(tag);
    }

    public static Predicate<Task> byTitle(String keyword) {
        return t -> t.getTitle().contains(keyword);
    }

    public static Predicate<Task> byDescription(String keyword) {
        return t -> t.getDescription().contains(keyword);
    }

    public static Predicate<Task> byCreationTime(LocalDate d) {
        return t -> t.getCreatedOn().compareTo(d) < 0;
    }

    public static List<Predicate<Task>> genPredicates(Function<String, Predicate<Task>> f, List<String> strs) {
    	List<Predicate<Task>> output = new ArrayList<Predicate<Task>>();
    	
    	for (String s : strs) {
    		output.add(f.apply(s));
    	}
    	
        return output;
    }

    public static Predicate<Task> andAll(List<Predicate<Task>> ps) {
    	// simpler way 
//    	return ps.stream().reduce(Predicate::and).orElse(x->true);
    	
    	// Conjunction solution on lecture slides
    	Predicate<Task> res = e -> true;	// default value evaluates to true
    	for (Predicate<Task> p : ps) {
    		res = res.and(p);
    	}
    	return res;
    }

    public static Predicate<Task> orAll(List<Predicate<Task>> ps) {
    	// simpler way
//    	return ps.stream().reduce(Predicate::or).orElse(x->false);
    	
    	// based on conjunction solution on lecture slides
    	Predicate<Task> res = e -> false;	// default value evaluates to true
    	for (Predicate<Task> p : ps) {
    		res = res.or(p);
    	}
    	return res;
    }

    public static Predicate<Task> not(Predicate<Task> p) {
    	return p.negate();
    }

    public static void main(String[] args) {
        init();
        System.out.println("OLK");

        Task task1 = new Task("ID1", "Read Version Control with Git book", TaskType.READING, LocalDate.of(2015, Month.JULY, 1)).addTag("git").addTag("reading").addTag("books");
        Task task2 = new Task("ID2", "Read Java 8 Lambdas book", TaskType.READING, LocalDate.of(2015, Month.JULY, 2)).addTag("java8").addTag("reading").addTag("books");
        Task task3 = new Task("ID3", "Write a mobile application to store my tasks", TaskType.CODING, LocalDate.of(2015, Month.JULY, 3)).addTag("coding").addTag("mobile");
        Task task4 = new Task("ID4", "Write a blog on Java 8 Streams", TaskType.WRITING, LocalDate.of(2014, Month.JULY, 4)).addTag("blogging").addTag("writing").addTag("streams");
        Task task5 = new Task("ID5", "Write a blog on Java 8 Streams", TaskType.WRITING, LocalDate.of(2016, Month.JULY, 7)).addTag("blogging").addTag("writing").addTag("streams");
        tasks.addAll(Arrays.asList(task1, task2, task3, task4, task5));
        Assert.assertEquals(tasks.get(4).toString(), task5.toString());

        /* Invoke the methods you defined */
        Task task6 = new Task("ID6", "Read Domain Driven Design book", TaskType.READING, LocalDate.of(2013, Month.JULY, 5)).addTag("ddd").addTag("books").addTag("reading");
        appendNewTask(task6);
        Assert.assertEquals(tasks.get(5).toString(), task6.toString());
        
        List sortedTags = findAndSortAllUniqueTags();
        /*
         * The assertion only checks the number of the sorted result.
         * Please manually check the content of the sorted list, which should contain
         * * "git", "reading", "books", "java8", "coding", "mobile", "blogging", "writing", "streams", "ddd"
         * * All the tags should be sorted according to the order of the string content
         */
        Assert.assertEquals(sortedTags.size(), 10);

        List writingTask = findTasks(byType(TaskType.WRITING));
        
        Assert.assertEquals(writingTask.size(), 2);
        Assert.assertEquals(writingTask.get(0).toString(), task4.toString());
        Assert.assertEquals(writingTask.get(1).toString(), task5.toString());

        int bloggingTagTaskNum = countTasks(byTag("blogging"));
        Assert.assertEquals(bloggingTagTaskNum, 2);

        List<Task> top2ReadingTasks = getTopNTasks(byType(TaskType.READING), 2);
        Assert.assertEquals(top2ReadingTasks.size(), 2);
        Assert.assertEquals(top2ReadingTasks.get(0).toString(), task1.toString());
        Assert.assertEquals(top2ReadingTasks.get(1).toString(), task2.toString());

        int readingTaskNum = countTasks(byType(TaskType.READING));
        Assert.assertEquals(readingTaskNum, 3);

        System.out.println();
        System.out.println("Before removal");
        for (Task t : tasks) {
        	System.out.println(t.getId());
        }
        
        boolean isRemoved = removeTask(byCreationTime(LocalDate.of(2014, Month.JULY, 4)));
        Assert.assertTrue(isRemoved);
        
        System.out.println();
        System.out.println("After removal");
        for (Task t : tasks) {
        	System.out.println(t.getId());
        }

        readingTaskNum = countTasks(byType(TaskType.READING));
        Assert.assertEquals(readingTaskNum, 2);
        
        /**
         * Need to test by description, by title, genpredicates, andall, orall, not, and combinations of everything
         */
        
        Task task7 = new Task("ID7", "Task 7 title here", "Test description", TaskType.READING, LocalDate.of(2013, Month.JULY, 5)).addTag("ddd").addTag("books").addTag("reading");
        Task task8 = new Task("ID8", "Task 8 title here", "Test description", TaskType.READING, LocalDate.of(2013, Month.JULY, 5)).addTag("ddd").addTag("books").addTag("reading");

        appendNewTask(task7);
        appendNewTask(task8);
        
        List descriptionTask = findTasks(byDescription("Test description"));
        
        System.out.println();
        System.out.println("Description Tasks: ");
        for (int i = 0; i < descriptionTask.size(); i++) {
        	System.out.println(descriptionTask.get(i));
        }
        
        Task task9 = new Task("ID9", "Test title here", "Task 9 description", TaskType.READING, LocalDate.of(2013, Month.JULY, 5)).addTag("ddd").addTag("books").addTag("reading");
        Task task10 = new Task("ID10", "Test title here", "Task 10 description", TaskType.READING, LocalDate.of(2013, Month.JULY, 5)).addTag("ddd").addTag("books").addTag("reading");

        appendNewTask(task9);
        appendNewTask(task10);
        
        List titleTask = findTasks(byTitle("Test title here"));
        
        System.out.println();
        System.out.println("Title Tasks: ");
        for (int i = 0; i < titleTask.size(); i++) {
        	System.out.println(titleTask.get(i));
        }
        
        Task task11 = new Task("ID11", "Task 11 title", "Task 11 description", TaskType.WRITING, LocalDate.of(2013, Month.JULY, 5)).addTag("coding").addTag("blogging").addTag("reading");
        Task task12 = new Task("ID12", "Task 12 title", "Task 12 description", TaskType.READING, LocalDate.of(2013, Month.JULY, 5)).addTag("ddd").addTag("books").addTag("reading");
        
        appendNewTask(task11);
        appendNewTask(task12);
        
//        List<String> keywords = Arrays.asList("blogging", "reading", "coding");
        List<String> keywords = Arrays.asList("books", "reading", "git");
        List gen = genPredicates(TaskManagementSystem::byTag, keywords);
        
        Predicate<Task> andTest = andAll(gen);
        System.out.println(andTest.test(task11));
        
        Predicate<Task> orTest = orAll(gen);
        System.out.println(orTest.test(task12));
        
        // Test not predicate
        System.out.println(not(orTest).test(task12));
        System.out.println(not(andTest).test(task11));
        
//        System.out.println("books removed:" + removeTask(byTag("books")));
//        System.out.println("books not removed:" + removeTask(not(byTag("books"))));
        
        System.out.println();
        for (Task t : tasks) {
        	System.out.println(t.getId() + " : " + t.getTags() + " : test for AND : " + andTest.test(t));
        	System.out.println(t.getId() + " : " + t.getTags() + " : test for OR : " + orTest.test(t));
        }
        
        // Nested genPredicates
        List<String> titleKeys = Arrays.asList("title", "write", "Read");
        List gen1 = genPredicates(TaskManagementSystem::byTitle, titleKeys);
        
        System.out.println();
        System.out.println("By Title gen Predicate test:");
        orTest = orAll(gen1);
        andTest = andAll(gen1);
        for (Task t : tasks) {
        	System.out.println(t.getId() + " : " + t.getTitle() + " : test for AND : " + andTest.test(t));
        	System.out.println(t.getId() + " : " + t.getTitle() + " : test for OR : " + orTest.test(t));
        }
        
        List<String> descKeywords = Arrays.asList("description", "Task");
        List gen2 = genPredicates(TaskManagementSystem::byDescription, descKeywords);
        
        System.out.println();
        System.out.println("By description gen predicate test:");
        orTest = orAll(gen2);
        andTest = andAll(gen2);
        for (Task t : tasks) {
        	System.out.println(t.getId() + " : " + t.getDescription() + " : test for AND : " + andTest.test(t));
        	System.out.println(t.getId() + " : " + t.getDescription() + " : test for OR : " + orTest.test(t));
        }
        
        System.out.println();
        System.out.println("Removing tasks if previous ANDs are true: " + removeTask(andTest));
    	
    	System.out.println();
    	System.out.println("After removal:");
    	for (Task t : tasks) {
    		System.out.println(t.getId() + " : " + t.getDescription() + " : test for AND : " + andTest.test(t));
    	}
    	
    	System.out.println();
        System.out.println("Removing by tag 'books': " + removeTask(byTag("books")));
        
        
        System.out.println();
    	System.out.println("After removal:");
    	for (Task t : tasks) {
    		System.out.println(t.getId() + " : " + t.getTags());
    	}
    }
}
