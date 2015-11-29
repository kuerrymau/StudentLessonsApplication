package com.crimore.model;

import com.crimore.model.Lessondays;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-11-20T20:38:32")
@StaticMetamodel(Lessons.class)
public class Lessons_ { 

    public static volatile SingularAttribute<Lessons, Integer> id;
    public static volatile SingularAttribute<Lessons, String> name;
    public static volatile CollectionAttribute<Lessons, Lessondays> lessondaysCollection;
    public static volatile SingularAttribute<Lessons, String> faculty;
    public static volatile SingularAttribute<Lessons, Integer> totalnoofstudentss;

}