package com.eqdushu.server.domain.book;

import com.eqdushu.server.domain.BaseDomain;

/**
 * Created by nobita on 17/10/14.
 */
public class BookInLibJrl extends BaseDomain{
	
	private static final long serialVersionUID = 7890112211L;

	private long userId;

    private long companyId;

    private long bookId;

    private String isbn;

    private String sts;//U登记状态  S成功   F  失败拒绝

    private String bookImg;

    private String bookTitle;

    private String bookAuthor;

    private String bookPublisher;

    private String bookSummary;

    private String name;

    private String borrowBegDt;

    private String borrowEndDt;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getBorrowBegDt() {
        return borrowBegDt;
    }

    public void setBorrowBegDt(String borrowBegDt) {
        this.borrowBegDt = borrowBegDt;
    }

    public String getBorrowEndDt() {
        return borrowEndDt;
    }

    public void setBorrowEndDt(String borrowEndDt) {
        this.borrowEndDt = borrowEndDt;
    }

    public String getBookImg() {
        return bookImg;
    }

    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookSummary() {
        return bookSummary;
    }

    public void setBookSummary(String bookSummary) {
        this.bookSummary = bookSummary;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
