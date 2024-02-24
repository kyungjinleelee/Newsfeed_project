package com.sparta.newsfeed.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.newsfeed.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import static com.sparta.newsfeed.domain.QBoard.board;


public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Board.class);
        this.queryFactory = queryFactory;
    }

    // 키워드 검색
    @Override
    public Page<Board> findByOption(String keyword, Pageable pageable) {
        var query = queryFactory.select(board)
                .from(board)
                .where(containContents(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        query.orderBy(board.createdAt.desc());

        var boards = query.fetch();
        long totalSize =  queryFactory.select(Wildcard.count)
                .from(board)
                .where(containContents(keyword))
                .fetch().get(0);
        return PageableExecutionUtils.getPage(boards, pageable, () -> totalSize);
    }

    // 유저 아이디로 검색
    @Override
    public Page<Board> findByUser(String username, Pageable pageable) {
        var query = queryFactory.select(board)
                .from(board)
                .where(eqUsername(username))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        query.orderBy(board.createdAt.desc());
        var boards = query.fetch();

        long totalSize = queryFactory.select(Wildcard.count)
                .from(board)
                .where(eqUsername(username))
                .fetch().get(0);
        return PageableExecutionUtils.getPage(boards, pageable, () -> totalSize);
    }

    // 유저 닉네임으로 검색
    @Override
    public Page<Board> findByName(String name, Pageable pageable) {
        var query = queryFactory.select(board)
                .from(board)
                .where(eqName(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        query.orderBy(board.createdAt.desc());
        var boards = query.fetch();

        long totalSize = queryFactory.select(Wildcard.count)
                .from(board)
                .where(eqName(name))
                .fetch().get(0);
        return PageableExecutionUtils.getPage(boards, pageable, () -> totalSize);
    }

    // ================================ 메서드 =======================================

    // 내용 키워드
    private BooleanExpression containContents(String contents) {
        if (contents == null || contents.isEmpty()) {
            return Expressions.asBoolean(true);
        }
        return board.contents.containsIgnoreCase(contents);
    }

    // 유저 아이디
    private BooleanExpression eqUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        return board.user.username.equalsIgnoreCase(username);
    }

    // 유저 닉네임
    private BooleanExpression eqName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return board.user.name.equalsIgnoreCase(name);
    }


}
