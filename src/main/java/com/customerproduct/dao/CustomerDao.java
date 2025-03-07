package com.customerproduct.dao;

import com.customerproduct.constants.AppConstants;
import com.customerproduct.dto.SearchDto;
import com.customerproduct.model.Customer;
import com.customerproduct.repository.CustomerRepository;
import com.customerproduct.utils.CustomerProductUtils;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDao implements CustomerRepository {
    @Autowired
    SessionFactory sessionFactory;

    private final Logger log =  LoggerFactory.getLogger(CustomerDao.class);

    @Override
    public boolean addOrUpdateCustomer(Customer customer) {
        Session session = sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.merge(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            log.error("Exception occurred at addOrUpdateCustomer() Method in Customer Dao " , e);
        } finally {
            if (session.isOpen()) session.close();
        }
        return false;
    }

    @Override
    @Cacheable(value = "customers", key = "#searchDTO.id")
    public List<Customer> getAllCustomers(SearchDto searchDto) {
        Session session = sessionFactory.openSession();
        List<Customer> customers = null;
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
            Root<Customer> root = criteriaQuery.from(Customer.class);
            Predicate clientPredicate = criteriaBuilder.equal(root.get("client"), searchDto.getClient());
            criteriaQuery.select(root).where(clientPredicate);
            customers = session.createQuery(criteriaQuery).setFirstResult(searchDto.getOffset() * searchDto.getLimit()).setMaxResults(searchDto.getLimit()).getResultList();
        } catch (Exception e) {
            log.error("Exception occurred at getAllCustomers() in Customer Dao " , e);
        } finally {
            if (session.isOpen()) session.close();
        }
        return customers;
    }

    @Override
//    @Cacheable(value = "customers", key = "{#searchDto.client, #searchDto.name, #searchDto.phoneNo, #searchDto.customerCode, #searchDto.email, #searchDto.lastModifiedDate, #searchDto.create_date, #searchDto.id}")
    public List<Customer> getCustomer(SearchDto searchDto) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Customer WHERE client = :client");

            if (CustomerProductUtils.isNotNullAndEmpty(searchDto.getName())) {
                hql.append(" AND name = :name");
            }
            if (CustomerProductUtils.isNotNullAndEmpty(searchDto.getPhoneNo())) {
                hql.append(" AND phoneNo = :phoneNo");
            }
            if (CustomerProductUtils.isNotNullAndEmpty(searchDto.getCustomerCode())) {
                hql.append(" AND customerCode = :customerCode");
            }
            if (CustomerProductUtils.isNotNullAndEmpty(searchDto.getEmail())) {
                hql.append(" AND email = :email");
            }
            if (searchDto.getLastModifiedDate() != null) {
                hql.append(" AND lastModifiedDate = :lastModifiedDate");
            }
            if (searchDto.getCreate_date() != null) {
                hql.append(" AND create_date = :create_date");
            }
            if (searchDto.getId() > 0) {
                hql.append(" AND id = :id");
            }

            Query query = session.createQuery(hql.toString(), Customer.class);
            query.setParameter("client", searchDto.getClient());

            if (CustomerProductUtils.isNotNullAndEmpty(searchDto.getName())) {
                query.setParameter("name", searchDto.getName());
            }
            if (CustomerProductUtils.isNotNullAndEmpty(searchDto.getPhoneNo())) {
                query.setParameter("phoneNo", searchDto.getPhoneNo());
            }
            if (CustomerProductUtils.isNotNullAndEmpty(searchDto.getCustomerCode())) {
                query.setParameter("customerCode", searchDto.getCustomerCode());
            }
            if (CustomerProductUtils.isNotNullAndEmpty(searchDto.getEmail())) {
                query.setParameter("email", searchDto.getEmail());
            }
            if (searchDto.getLastModifiedDate() != null) {
                query.setParameter("lastModifiedDate", searchDto.getLastModifiedDate());
            }
            if (searchDto.getCreate_date() != null) {
                query.setParameter("create_date", searchDto.getCreate_date());
            }
            if (searchDto.getId() > 0) {
                query.setParameter("id", searchDto.getId());
            }

            List<Customer> customers = query.getResultList();
            log.info("List of customers are : {} ",customers);
            return customers;
        } catch (Exception e) {
            log.error("Exception occurred at getCustomers() in Customer Dao " ,e);
            return null;
        }
    }

    @Override
    @KafkaListener(topics = AppConstants.CUSTOMER_ADD_UPDATE_TOPIC_NAME, groupId = AppConstants.GROUP_ID, containerFactory = "customerListener")
    public boolean addOrUpdateCustomerBulk(Customer customer) {
        Session session = sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.merge(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
             log.error("Exception occurred at bulkAddOrUpdateCustomer() in Customer Dao " , e);
        } finally {
            if (session.isOpen()) session.close();
        }
        return false;
    }
}
