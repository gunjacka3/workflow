package cn.com.workflow.common.vo;

import java.io.Serializable;
import java.util.List;


/**
 * 典型Pojo对象
 * 有四个属性：x，y，width，height
 * @author Administrator
 *
 */
public class ActiveNode implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ActiveNode(String _name,int _x,int _y,int _width,int _height)
	{
		this.setX(_x);
		this.setY(_y);
		this.setWidth(_width);
		this.setHeight(_height);
	}
	public ActiveNode(){
		
	}
	private boolean enableFlag;
	private int x;
	private int y;
	private int width;
	private int height;
	private List<ActiveNode> activeNodes;
	private String id;
	private String name;
	private int parentX;
	private int parentY;
	private int parentW;
	private int parentH;
	private String parentId;
	
	
	
	public boolean isEnableFlag() {
		return enableFlag;
	}
	public void setEnableFlag(boolean enableFlag) {
		this.enableFlag = enableFlag;
	}
	public int getParentX() {
		return parentX;
	}
	public void setParentX(int parentX) {
		this.parentX = parentX;
	}
	public int getParentY() {
		return parentY;
	}
	public void setParentY(int parentY) {
		this.parentY = parentY;
	}
	public int getParentW() {
		return parentW;
	}
	public void setParentW(int parentW) {
		this.parentW = parentW;
	}
	public int getParentH() {
		return parentH;
	}
	public void setParentH(int parentH) {
		this.parentH = parentH;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	//[end]----------属性定义---------
	public List<ActiveNode> getActiveNodes() {
		return activeNodes;
	}
	public void setActiveNodes(List<ActiveNode> activeNodes) {
		this.activeNodes = activeNodes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
