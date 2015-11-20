/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimore.model;

import com.crimore.model.exceptions.NonexistentEntityException;
import com.crimore.model.exceptions.PreexistingEntityException;
import com.crimore.model.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Chayanne
 */
public class LessondaysJpaController implements Serializable
{

    public LessondaysJpaController(UserTransaction utx, EntityManagerFactory emf)
    {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Lessondays lessondays) throws PreexistingEntityException, RollbackFailureException, Exception
    {
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            Lessons lessonid = lessondays.getLessonid();
            if (lessonid != null)
            {
                lessonid = em.getReference(lessonid.getClass(), lessonid.getId());
                lessondays.setLessonid(lessonid);
            }
            em.persist(lessondays);
            if (lessonid != null)
            {
                lessonid.getLessondaysCollection().add(lessondays);
                lessonid = em.merge(lessonid);
            }
            utx.commit();
        } catch (Exception ex)
        {
            try
            {
                utx.rollback();
            } catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findLessondays(lessondays.getId()) != null)
            {
                throw new PreexistingEntityException("Lessondays " + lessondays + " already exists.", ex);
            }
            throw ex;
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public void edit(Lessondays lessondays) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            Lessondays persistentLessondays = em.find(Lessondays.class, lessondays.getId());
            Lessons lessonidOld = persistentLessondays.getLessonid();
            Lessons lessonidNew = lessondays.getLessonid();
            if (lessonidNew != null)
            {
                lessonidNew = em.getReference(lessonidNew.getClass(), lessonidNew.getId());
                lessondays.setLessonid(lessonidNew);
            }
            lessondays = em.merge(lessondays);
            if (lessonidOld != null && !lessonidOld.equals(lessonidNew))
            {
                lessonidOld.getLessondaysCollection().remove(lessondays);
                lessonidOld = em.merge(lessonidOld);
            }
            if (lessonidNew != null && !lessonidNew.equals(lessonidOld))
            {
                lessonidNew.getLessondaysCollection().add(lessondays);
                lessonidNew = em.merge(lessonidNew);
            }
            utx.commit();
        } catch (Exception ex)
        {
            try
            {
                utx.rollback();
            } catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = lessondays.getId();
                if (findLessondays(id) == null)
                {
                    throw new NonexistentEntityException("The lessondays with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            Lessondays lessondays;
            try
            {
                lessondays = em.getReference(Lessondays.class, id);
                lessondays.getId();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The lessondays with id " + id + " no longer exists.", enfe);
            }
            Lessons lessonid = lessondays.getLessonid();
            if (lessonid != null)
            {
                lessonid.getLessondaysCollection().remove(lessondays);
                lessonid = em.merge(lessonid);
            }
            em.remove(lessondays);
            utx.commit();
        } catch (Exception ex)
        {
            try
            {
                utx.rollback();
            } catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public List<Lessondays> findLessondaysEntities()
    {
        return findLessondaysEntities(true, -1, -1);
    }

    public List<Lessondays> findLessondaysEntities(int maxResults, int firstResult)
    {
        return findLessondaysEntities(false, maxResults, firstResult);
    }

    private List<Lessondays> findLessondaysEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lessondays.class));
            Query q = em.createQuery(cq);
            if (!all)
            {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally
        {
            em.close();
        }
    }

    public Lessondays findLessondays(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Lessondays.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getLessondaysCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lessondays> rt = cq.from(Lessondays.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}
