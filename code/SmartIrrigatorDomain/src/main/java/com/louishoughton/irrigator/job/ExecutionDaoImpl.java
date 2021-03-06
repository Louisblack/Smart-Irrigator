package com.louishoughton.irrigator.job;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExecutionDaoImpl implements ExecutionDao {

    private SessionFactory sessionFactory;


    @Autowired
    public ExecutionDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void save(Execution execution) {
        execution.setDateRun(new Date());
        sessionFactory.getCurrentSession().save(execution);
    }

    @Transactional
    public Execution get(int id) {
        return (Execution) sessionFactory.getCurrentSession().get(Execution.class, id);
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Execution> get(List<Integer> ids) {
        return sessionFactory.getCurrentSession()
                .createCriteria(Execution.class)
                .add(Restrictions.in("id", ids))
                .list();
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Execution> list() {
        return sessionFactory.getCurrentSession()
                .createCriteria(Execution.class)
                .addOrder(Order.desc("dateRun"))
                .list();
    }


    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Execution> list(int from, int to) {
        return sessionFactory.getCurrentSession()
                .createCriteria(Execution.class)
                .setFirstResult(from)
                .setMaxResults(to - from)
                .addOrder(Order.desc("dateRun"))
                .list();
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Execution> getExecutionsBetween(Date from, Date to) {
        return sessionFactory.getCurrentSession()
                .createCriteria(Execution.class)
                .add(Restrictions.between("dateRun", from, to))
                .addOrder(Order.desc("dateRun"))
                .list();
    }
}
