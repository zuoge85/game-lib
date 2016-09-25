package com.isnowfox.util.collect;


/**
 * 物品接口
 * 
 * @author zuoge85
 * 
 */
public class CoreGoods{
	protected int id;
	protected int nums;
	/**
	 * 强化等级等等
	 */
	protected int level;

	/**
	 * 品阶级
	 */
	protected int grade;

	/**
	 * 强化进度
	 */
	protected int levUpProgress;
	
	

	public CoreGoods() {

	}

	public CoreGoods(int id) {
		this.id = id;
	}

	public CoreGoods(int id, int nums) {
		this.id = id;
		this.nums = nums;
	}

	public CoreGoods(int id, int nums, int level, int grade) {
		this.id = id;
		this.nums = nums;
		this.grade = grade;
		this.level = level;
	}
	
	public CoreGoods(CoreGoods e) {
		this.id = e.id;
		this.nums = e.nums;
		this.grade = e.grade;
		this.level = e.level;
	}
	
	/**
	 * 表示类型
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}

	public int getGrade() {
		return grade;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getLevUpProgress() {
		return levUpProgress;
	}

	public void setLevUpProgress(int levUpProgress) {
		this.levUpProgress = levUpProgress;
	}

	@Override
	public String toString() {
		return "CoreGoods [id=" + id + ", nums=" + nums + ", level=" + level
				+ ", grade=" + grade + "]";
	}
}
