/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimore.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chayanne
 */
@Stateless
public class LessondaysFacade extends AbstractFacade<Lessondays>
{
    @PersistenceContext(unitName = "StudentLessonsApplicationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager()
    {
        return em;
    }

    public LessondaysFacade()
    {
        super(Lessondays.class);
    }
    
}
