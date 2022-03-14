package org.bng.blog;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import io.quarkus.panache.common.Sort;

import org.bng.data.Blog;
import org.bng.data.Category;
import org.bng.data.User;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/blogs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BlogResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    EntityManager em;

    @Claim(standard = Claims.preferred_username)
    String username;

    @GET
    @Path("/posts")
    public List<Blog> getBlogs(@Context SecurityContext ct) {
        System.out.println("LBuser: " + ct.getUserPrincipal().getName());
        String uname = ct.getUserPrincipal().getName();
        return Blog.find("author", uname).list();
    }

    @GET
    @Path("/allposts")
    public List<Blog> getAllBlogs(@Context SecurityContext ct) {
        System.out.println("LBuser: " + ct.getUserPrincipal().getName());
        String uname = ct.getUserPrincipal().getName();
        TypedQuery<Blog> q = em.createQuery("select t from Blog t where t.author <> (:author)",
                Blog.class);
        q.setParameter("author", uname);
        return q.getResultList();
    }

    @POST
    @Path("/post/{category_id}")
    @Transactional
    public void addBlog(@Context SecurityContext ct, @PathParam("category_id") Long category_id, Blog blog) {
        String uname = ct.getUserPrincipal().getName();
        blog.setAuthor(uname);
        Category category = Category.findById(category_id);
        blog.setCategory(category);
        Blog.persist(blog);
    }

    @PUT
    @Path("/post/{id}/{category_id}")
    @Transactional
    public void updateBlog(@Context SecurityContext ct,@PathParam("id") Long id, @PathParam("category_id") Long category_id, Blog blog) {
        System.out.println("id:" + id);
        System.out.println("title:" + blog.getTitle());
        Blog blog1 = Blog.find("id", id).firstResult();
        Category category = Category.findById(category_id);
        String uname = ct.getUserPrincipal().getName();
        blog.setAuthor(uname);
        blog1.setAuthor(blog.getAuthor());
        blog1.setBody(blog.getBody());
        blog1.setTitle(blog.getTitle());
        blog1.setCategory(category);
        Blog.persist(blog1);
    }

    @DELETE
    @Path("/post/{id}")
    @Transactional
    public void deleteBlog(@PathParam("id") Long id) {
        Blog blog = Blog.find("id", id).firstResult();
        blog.delete();

    }

}
