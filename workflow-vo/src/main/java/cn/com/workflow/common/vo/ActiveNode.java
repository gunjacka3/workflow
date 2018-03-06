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
	public ActiveNode(String _name,double _x,double _y,double _width,double _height)
	{
		this.setX(_x);
		this.setY(_y);
		this.setWidth(_width);
		this.setHeight(_height);
	}
	public ActiveNode(){
		
	}
	private boolean enableFlag;
	private double x;
	private double y;
	private double width;
	private double height;
	private List<ActiveNode> activeNodes;
	private String id;
	private String name;
	private double parentX;
	private double parentY;
	private double parentW;
	private double parentH;
	private String parentId;
	
	
	
	public boolean isEnableFlag() {
		return enableFlag;
	}
	public void setEnableFlag(boolean enableFlag) {
		this.enableFlag = enableFlag;
	}
	public double getParentX() {
		return parentX;
	}
	public void setParentX(double parentX) {
		this.parentX = parentX;
	}
	public double getParentY() {
		return parentY;
	}
	public void setParentY(double parentY) {
		this.parentY = parentY;
	}
	public double getParentW() {
		return parentW;
	}
	public void setParentW(double parentW) {
		this.parentW = parentW;
	}
	public double getParentH() {
		return parentH;
	}
	public void setParentH(double parentH) {
		this.parentH = parentH;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public double getX()
	{
		return x;
	}
	public void setX(double x)
	{
		this.x = x;
	}
	public double getY()
	{
		return y;
	}
	public void setY(double y)
	{
		this.y = y;
	}
	public double getWidth()
	{
		return width;
	}
	public void setWidth(double width)
	{
		this.width = width;
	}
	public double getHeight()
	{
		return height;
	}
	public void setHeight(double height)
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
