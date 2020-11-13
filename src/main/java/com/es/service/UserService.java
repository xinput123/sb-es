package com.es.service;import com.es.api.ElasticSearchDocument;import com.es.api.ElasticSearchRestApi;import com.es.entity.User;import com.es.exception.ServiceException;import org.elasticsearch.index.query.BoolQueryBuilder;import org.elasticsearch.index.query.QueryBuilders;import org.elasticsearch.search.builder.SearchSourceBuilder;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import org.springframework.util.CollectionUtils;import java.io.IOException;import java.util.ArrayList;import java.util.List;/** * @author <a href="mailto:xinput.xx@gmail.com">xinput</a> * @date 2020-11-13 11:58 */@Servicepublic class UserService {    private static final String ES_INDEX = "test_user";    @Autowired    private ElasticSearchRestApi elasticSearchRestApi;    /**     * 保存用户信息     */    public void save(User user) {        ElasticSearchDocument<User> document = new ElasticSearchDocument<>();        document.setId(user.getId().toString())                .setData(user);        try {            elasticSearchRestApi.save(ES_INDEX, document);        } catch (IOException e) {            throw new ServiceException("Failed to save user, id: " + user.getId(), e);        }    }    /**     * 批量保存用户     *     * @param users 用户信息集合     */    public void saveAllUser(List<User> users) {        if (CollectionUtils.isEmpty(users)) {            return;        }        List<ElasticSearchDocument<User>> documentList = new ArrayList<>(users.size());        ElasticSearchDocument<User> document = null;        for (User user : users) {            document = new ElasticSearchDocument<>();            document.setId(String.valueOf(user.getId()));            document.setData(user);            documentList.add(document);        }        try {            elasticSearchRestApi.saveAll(ES_INDEX, documentList);        } catch (IOException e) {            throw new ServiceException("Failed to batch save users", e);        }    }    /**     * 根据用户 ID 获取用户信息     *     * @param id 用户 ID     * @return 用户对象     */    public User getUser(String id) {        try {            User user = elasticSearchRestApi.get(ES_INDEX, id, User.class);            return user;        } catch (IOException e) {            throw new ServiceException("Failed to get user, id: " + id, e);        }    }    /**     * 根据用户姓名查询     *     * @param name 用户姓名     * @return 用户集合     */    public List<User> searchUserByName(String name) {        // 构建查询条件        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()                .must(QueryBuilders.matchPhraseQuery("name", name));        // 构建查询生成器        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(boolQueryBuilder);        try {            List<User> userList = elasticSearchRestApi.searchByQuery(ES_INDEX, sourceBuilder, User.class);            return userList;        } catch (IOException e) {            throw new ServiceException("Failed to search user by name: " + name, e);        }    }    /**     * 根据用户 ID 删除用户信息     *     * @param id 用户 ID     */    public void deleteUser(String id) {        try {            elasticSearchRestApi.delete(ES_INDEX, id);        } catch (IOException e) {            throw new ServiceException("Failed to delete user by id: " + id, e);        }    }    /**     * 根据用户姓名删除     *     * @param name 用户姓名     */    public void deleteUserByName(String name) {        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()                .must(QueryBuilders.matchPhraseQuery("name", name));        try {            elasticSearchRestApi.deleteByQuery(ES_INDEX, boolQueryBuilder);        } catch (IOException e) {            throw new ServiceException("Failed to delete user by name: " + name, e);        }    }}