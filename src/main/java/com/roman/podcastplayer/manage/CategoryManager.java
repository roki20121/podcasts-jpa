package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Category;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class CategoryManager {

    @PersistenceContext(unitName = "com.roman.podcasts")
    private EntityManager manager;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveCategory(Category category) {
        manager.persist(category);
    }

    public Integer createCategory(String name) {
        Category lookupCategory = findCategoryByName(name);

        if (lookupCategory != null) {
            return lookupCategory.getId();
        }

        Category category = new Category(name);
        manager.persist(category);
        return category.getId();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteCategory(Category category) {
        manager.remove(category);
    }

    public Category findCategoryById(Integer id) {
        return manager.find(Category.class, id);
    }

    public Category findCategoryByName(String name) {
        TypedQuery<Category> query = manager.createQuery("select c from Category c where c.name = :name", Category.class);
        query.setParameter("name", name);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

}
