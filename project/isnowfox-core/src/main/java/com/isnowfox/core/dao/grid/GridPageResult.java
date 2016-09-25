package com.isnowfox.core.dao.grid;


import java.util.List;

import com.isnowfox.core.PageResult;


public abstract class GridPageResult<T> extends PageResult<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3198462620513733571L;
	
	public abstract Columns getColumns();
	
	public static final <T> GridPageResult<T> createGridPage(final int count,final int page,final int pageSize,final Columns columns,final List<T> list){
		int m=count%pageSize;
		int s=count/pageSize;
		final int pageCount= m>0?++s:s;
		return new GridPageResult<T>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6863895606890210253L;
			private List<T> value = list;
			@Override
			public int getPageCount(){
				return pageCount;
			}
			@Override
			public void setValue(List<T> value) {
				this.value=value;
			}
			@Override
			public int getStart(){
				return (page-1)*pageSize;
			}
			@Override
			public List<T> getValue() {
				return value;
			}
			@Override
			public int getCount() {
				return count;
			}
			@Override
			public int getPage() {
				return page;
			}
			@Override
			public int getPageSize() {
				return pageSize;
			}
			@Override
			public boolean isHasPrev(){
				return page>1;
			}
			@Override
			public boolean isHasNext(){
				return page<pageCount;
			}
			@Override
			public String toString() {
				return "PageResult [count=" + count + ", page=" + page + ", pageSize="
						+ pageSize + ", value=" + value + ", columns[" + columns + "]]";
			}
			@Override
			public Columns getColumns() {
				return columns;
			}
		};
	}
}
