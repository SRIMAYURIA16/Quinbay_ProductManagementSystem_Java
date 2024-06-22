import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import static com.mongodb.client.model.Updates.set;

public class CategoryManagement {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> collection;

    CategoryManagement(){
        mongoClient= MongoClients.create("mongodb://localhost:27017");
        mongoDatabase = mongoClient.getDatabase("Training");
        collection= mongoDatabase.getCollection("Category");
    }

    public void addCategory(String categoryName){
        Document maxId=collection.find().sort(new Document("categoryId",-1)).first();
        int categoryId= maxId==null?1:maxId.getInteger("categoryId",0)+1;
        Category category=new Category(categoryId,categoryName);
        Document find= collection.find(Filters.eq("categoryName",categoryName)).first();

        if(find==null) {
            Document categoryDocument = new Document("categoryId",categoryId)
                    .append("categoryName", category.getCategoryName());
            collection.insertOne(categoryDocument);
            System.out.println("Category added successfully");
        }
        else{
            System.out.println("Category Existed already");
        }
    }

    public void viewCategory(){
        FindIterable<Document> category=collection.find() ;
        if(category.first()!=null){
            System.out.println("Available Categories");
            for (Document cat:category){
                printDocument(cat);
            }
        }
        else{
            System.out.println("No categories available");
        }
    }

    public void printDocument(Document cat){
        System.out.println("Category Id: "+cat.getInteger("categoryId"));
        System.out.println("Category Name: "+cat.getString("categoryName"));
        System.out.println("=================");
    }

    public void deleteCategory(int categoryId){
        Document doc=collection.find(Filters.eq("categoryId",categoryId)).first();
        if(doc!=null){
            collection.deleteOne(Filters.eq("categoryId",categoryId));
            System.out.println("Category deleted successfully!!!");
        }
        else{
            System.out.println("No category available in this id");
        }
    }

    public void updateCategoryName(int categoryId,String newName){
        Document doc=collection.find(Filters.eq("categoryId",categoryId)).first();
        if(doc!=null){
            collection.updateOne(Filters.eq("categoryId",categoryId), set("categoryName",newName));
            System.out.println("Category Name updated successfully");
        }
        else{
            System.out.println("No category available in this Id");
        }
    }
}
