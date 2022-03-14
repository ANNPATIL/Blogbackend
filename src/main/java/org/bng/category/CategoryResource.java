package org.bng.category;

import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import io.quarkus.panache.common.Sort;

import org.bng.data.Category;

@Path("/blogs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {
    @GET
    @Path("/categories")
    public List<Category> getCategories() {
        return Category.listAll(Sort.by("category").ascending());
    }

    @POST
    @Path("/category")
    @Transactional
    public void addCategory(Category category) {
        Category.persist(category);
    }

    @PUT
    @Path("/category/{id}")
    @Transactional
    public void updateCategory(@PathParam("id") Long id, Category category) {
        System.out.println("id:" + id);
        Category blog1 = Category.find("id", id).firstResult();
        blog1.setCategory(category.getCategory());
        Category.persist(blog1);
    }

    @DELETE
    @Path("/category/{id}")
    @Transactional
    public void deleteCategory(@PathParam("id") Long id) {
        Category category = Category.find("id", id).firstResult();
        category.delete();

    }

}
