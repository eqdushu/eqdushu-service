package com.eqdushu.server.mapper.book;

import com.eqdushu.server.domain.book.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nobita on 17/9/16.
 */
@Repository
public interface BookMapper {
    /**
     * 获取公司的书库信息
     * @param companyId
     * @return
     */
    public List<Book> getLibrary(long companyId);

    /**
     * 通过ISBN号获取书籍详情
     * @param isbn
     * @return
     */
    public Book getBookByIsbn(String isbn);

    /**
     * 减少公司对应原有书籍
     * @param bookBorrow
     * @return
     */
    public int deleteNumByCompanyId(BookBorrow bookBorrow);

    /**
     * 增加书籍信息
     * @param book
     * @return
     */
    public int createBook(Book book);

    /**
     * 记录一条借阅的流水信息
     * @param bookBorrow
     * @return
     */
    public int createBorrow(BookBorrow bookBorrow);

    /**
     * 记录企业与书籍关系信息
     * @param bookRelation
     * @return
     */
    public int createBookRelation(BookRelation bookRelation);
    /**
     * 通过企业ID分页查询该企业的借阅信息
     * @param page
     * @return
     */
    public List<BookBorrow> getBorrowInfoByCompanyId(PageQuery page);

    /**
     * 通过用户ID分页查询该用户的借阅信息
     * @param page
     * @return
     */
    public List<BookBorrow> getBorrowInfoByUsrId(PageQuery page);

    /**
     * 通过企业id和isbn查询关系信息
     * @param bookRelation
     * @return
     */
    public BookRelation getBookRelationByCompanyIdIsbn(BookRelation bookRelation);
    /**
     * 修改用户借阅书籍状态信息
     * @param bookBorrow
     * @return
     */
    public int updateBorrowSts(BookBorrow bookBorrow);

    /**
     * 还书增加关系中的数量
     * @param bookBorrow
     * @return
     */
    public int addNumByCompanyId(BookBorrow bookBorrow);

    /**
     * 增加企业对应书籍所有数量
     * @param bookRelation
     * @return
     */
    public int addAllNumByCompanyId(BookRelation bookRelation);

    /**
     * 增加申请入库流水
     * @param bookInLibJrl
     * @return
     */
    public int createInLibReq(BookInLibJrl bookInLibJrl);

    /**
     * 管理员查询所有申请入库的未处理信息
     * @return
     */
    public List<BookInLibJrl> getAllInLibJrl(long companyId);

    /**
     * 修改状态
     * @param jrl
     * @return
     */
    public int updReqSts(BookInLibJrl jrl);

    public List<BookRemind> getNeedRemind();
}
