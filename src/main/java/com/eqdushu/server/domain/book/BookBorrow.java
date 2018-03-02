package com.eqdushu.server.domain.book;

import com.eqdushu.server.domain.BaseDomain;

/**
 * Created by nobita on 17/9/16.
 */
public class BookBorrow extends BaseDomain {

	private static final long serialVersionUID = 33456771231L;

	private long userId;

    private long companyId;

    private long bookId;

    private String isbn;

    private String bookImg;

    private String bookTitle;

    private String bookAuthor;

    private String borrowSts;

    private String borrowBegDt;

    private String borrowBegTm;

    private String borrowEndDt;

    private String borrowEndTm;

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

    public String getBorrowSts() {
        return borrowSts;
    }

    public void setBorrowSts(String borrowSts) {
        this.borrowSts = borrowSts;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }


    public String getBorrowBegDt() {
        return borrowBegDt;
    }

    public void setBorrowBegDt(String borrowBegDt) {
        this.borrowBegDt = borrowBegDt;
    }

    public String getBorrowBegTm() {
        return borrowBegTm;
    }

    public void setBorrowBegTm(String borrowBegTm) {
        this.borrowBegTm = borrowBegTm;
    }

    public String getBorrowEndDt() {
        return borrowEndDt;
    }

    public void setBorrowEndDt(String borrowEndDt) {
        this.borrowEndDt = borrowEndDt;
    }

    public String getBorrowEndTm() {
        return borrowEndTm;
    }

    public void setBorrowEndTm(String borrowEndTm) {
        this.borrowEndTm = borrowEndTm;
    }


}
