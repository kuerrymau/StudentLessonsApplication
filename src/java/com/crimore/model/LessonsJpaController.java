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
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Chayanne
 */
public class LessonsJpaController implements Serializable
{

    public LessonsJpaController(UserTransaction utx, EntityManagerFactory emf)
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

    public void create(Lessons lessons) throws PreexistingEntityException, RollbackFailureException, Exception
    {
        if (lessons.getLessondaysCollection() == null)
        {
            lessons.setLessondaysCollection(new ArrayList<Lessondays>());
        }
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            Collection<Lessondays> attachedLessondaysCollection = new ArrayList<>();
            for (Lessondays lessondaysCollectionLessondaysToAttach : lessons.getLessondaysCollection())
            {
                lessondaysCollectionLessondaysToAttach = em.getReference(lessondaysCollectionLessondaysToAttach.getClass(), lessondaysCollectionLessondaysToAttach.getId());
                attachedLessondaysCollection.add(lessondaysCollectionLessondaysToAttach);
            }
            lessons.setLessondaysCollection(attachedLessondaysCollection);
            em.persist(lessons);
            for (Lessondays lessondaysCollectionLessondays : lessons.getLessondaysCollection())
            {
                Lessons oldLessonidOfLessondaysCollectionLessondays = lessondaysCollectionLessondays.getLessonid();
                lessondaysCollectionLessondays.setLessonid(lessons);
                lessondaysCollectionLessondays = em.merge(lessondaysCollectionLessondays);
                if (oldLessonidOfLessondaysCollectionLessondays != null)
                {
                    oldLessonidOfLessondaysCollectionLessondays.getLessondaysCollection().remove(lessondaysCollectionLessondays);
                    oldLessonidOfLessondaysCollectionLessondays = em.merge(oldLessonidOfLessondaysCollectionLessondays);
                }
            }
            utx.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex)
        {
            try
            {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findLessons(lessons.getId()) != null)
            {
                throw new PreexistingEntityException("Lessons " + lessons + " already exists.", ex);
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

    public void edit(Lessons lessons) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        EntityManager em = null;
        try
        {
            utx.begin();
            em = getEntityManager();
            Lessons persistentLessons = em.find(Lessons.class, lessons.getId());
            Collection<Lessondays> lessondaysCollectionOld = persistentLessons.getLessondaysCollection();
            Collection<Lessondays> lessondaysCollectionNew = lessons.getLessondaysCollection();
            Collection<Lessondays> attachedLessondaysCollectionNew = new ArrayList<>();
            for (Lessondays lessondaysCollectionNewLessondaysToAttach : lessondaysCollectionNew)
            {
                lessondaysCollectionNewLessondaysToAttach = em.getReference(lessondaysCollectionNewLessondaysToAttach.getClass(), lessondaysCollectionNewLessondaysToAttach.getId());
                attachedLessondaysCollectionNew.add(lessondaysCollectionNewLessondaysToAttach);
            }
            lessondaysCollectionNew = attachedLessondaysCollectionNew;
            lessons.setLessondaysCollection(lessondaysCollectionNew);
            lessons = em.merge(lessons);
            for (Lessondays lessondaysCollectionOldLessondays : lessondaysCollectionOld)
            {
                if (!lessondaysCollectionNew.contains(lessondaysCollectionOldLessondays))
                {
                    lessondaysCollectionOldLessondays.setLessonid(null);
                    lessondaysCollectionOldLessondays = em.merge(lessondaysCollectionOldLessondays);
                }
            }
            for (Lessondays lessondaysCollectionNewLessondays : lessondaysCollectionNew)
            {
                if (!lessondaysCollectionOld.contains(lessondaysCollectionNewLessondays))
                {
                    Lessons oldLessonidOfLessondaysCollectionNewLessondays = lessondaysCollectionNewLessondays.getLessonid();
                    lessondaysCollectionNewLessondays.setLessonid(lessons);
                    lessondaysCollectionNewLessondays = em.merge(lessondaysCollectionNewLessondays);
                    if (oldLessonidOfLessondaysCollectionNewLessondays != null && !oldLessonidOfLessondaysCollectionNewLessondays.equals(lessons))
                    {
                        oldLessonidOfLessondaysCollectionNewLessondays.getLessondaysCollection().remove(lessondaysCollectionNewLessondays);
                        oldLessonidOfLessondaysCollectionNewLessondays = em.merge(oldLessonidOfLessondaysCollectionNewLessondays);
                    }
                }
            }
            utx.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex)
        {
            try
            {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = lessons.getId();
                if (findLessons(id) == null)
                {
                    throw new NonexistentEntityException("The lessons with id " + id + " no longer exists.");
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
            Lessons lessons;
            try
            {
                lessons = em.getReference(Lessons.class, id);
                lessons.getId();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The lessons with id " + id + " no longer exists.", enfe);
            }
            Collection<Lessondays> lessondaysCollection = lessons.getLessondaysCollection();
            for (Lessondays lessondaysCollectionLessondays : lessondaysCollection)
            {
                lessondaysCollectionLessondays.setLessonid(null);
                lessondaysCollectionLessondays = em.merge(lessondaysCollectionLessondays);
            }
            em.remove(lessons);
            utx.commit();
        } catch (NotSupportedException | SystemException | NonexistentEntityException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex)
        {
            try
            {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException re)
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

    public List<Lessons> findLessonsEntities()
    {
        return findLessonsEntities(true, -1, -1);
    }

    public List<Lessons> findLessonsEntities(int maxResults, int firstResult)
    {
        return findLessonsEntities(false, maxResults, firstResult);
    }

    private List<Lessons> findLessonsEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lessons.class));
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

    public Lessons findLessons(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Lessons.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getLessonsCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lessons> rt = cq.from(Lessons.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}
