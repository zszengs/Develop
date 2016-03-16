package blog.base;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;

import blog.pojo.Learn;
import blog.pojo.Life;
import blog.pojo.Message;
import blog.service.LearnService;
import blog.service.LearnsortService;
import blog.service.LifeService;

import blog.service.LearncommentService;
import blog.service.LifecommentService;
import blog.service.LifesortService;
import blog.service.MessageService;
import blog.service.ResumeService;
import blog.service.TalkService;
import blog.service.UsersService;
import blog.util.QueryHelper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>
{

	// =============== ModelDriven的支持 ==================

	protected T model;

	public BaseAction()
	{
		try
		{
			// 通过反射获取model的真实类型
			ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
			Class<T> clazz = (Class<T>) pt.getActualTypeArguments()[0];
			// 通过反射创建model的实例
			model = clazz.newInstance();
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public T getModel()
	{
		return model;
	}

	// =============== Service实例的声明 ==================
	@Resource
	public LearnService learnService;
	@Resource
	public LearnsortService learnsortService;
	@Resource
	public LifeService lifeService;
	@Resource
	public LifesortService lifesortService;
	@Resource
	public MessageService messageService;
	@Resource
	public TalkService talkService;
	@Resource
	public UsersService usersService;
	@Resource
	public ResumeService resumeService;
	@Resource
    public LifecommentService lifecommentService;
	@Resource
	public LearncommentService learncommentService;
	// ============== 分页用的参数 =============

	protected int pageNum = 1; // 当前页
	protected int pageSize = 15; // 每页显示多少条记录

	public int getPageNum()
	{
		return pageNum;
	}

	public void setPageNum(int pageNum)
	{
		this.pageNum = pageNum;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	// ===================点击排行版生活==========================

	public void buildRankLife(Integer totalNum)
	{
		// -----------点击排行榜-------------------------
		String hql = new QueryHelper(Life.class, "")// l别名
				.addOrderProperty("hits", false)// 按点击量降序
				.getListQueryHql();
		List<Life> lifes = lifeService.getByHql(hql);

		if (lifes.size() <= totalNum)// 前台显示前totalNum条
		{
			ActionContext.getContext().put("lifeRank", lifes);
		} else
		{
			ActionContext.getContext().put("lifeRank", lifes.subList(0, totalNum));
		}
	}

	// 点赞排行
	public void lifePraise(Integer totalNum)
	{
		// -----------点赞排行-------------------------
		String hql = new QueryHelper(Life.class, "")// l别名
				.addOrderProperty("praise", false)// 按点赞排行降序
				.getListQueryHql();
		List<Life> lifes = lifeService.getByHql(hql);

		if (lifes.size() <= totalNum)// 前台显示前totalNum条
		{
			ActionContext.getContext().put("lifePraise", lifes);
		} else
		{
			ActionContext.getContext().put("lifePraise", lifes.subList(0, totalNum));
		}
	}

	public void buildNewLife(Integer totalNum)
	{
		// -----------最新文章-------------------------
		String hql = new QueryHelper(Life.class, "")// l别名
				.addOrderProperty("uploadTime", false)// 按日期降序
				.getListQueryHql();
		List<Life> lifes = lifeService.getByHql(hql); // 日期降序集合

		if (lifes.size() <= totalNum)// 前台显示前totalNum条
		{
			ActionContext.getContext().put("lifenew", lifes);
		} else
		{
			ActionContext.getContext().put("lifenew", lifes.subList(0, totalNum));
		}
	}

	// ===================点击排行版学习==========================
	public void buildRankLearn(Integer totalNum)
	{
		// -----------点击排行榜-------------------------
		String hql = new QueryHelper(Learn.class, "")// l别名
				.addOrderProperty("hits", false)// 按点击量降序
				.getListQueryHql();
		List<Learn> learns = learnService.getByHql(hql);

		if (learns.size() <= totalNum)// 前台显示前totalNum条
		{
			ActionContext.getContext().put("learnRank", learns);
		} else
		{
			ActionContext.getContext().put("learnRank", learns.subList(0, totalNum));

		}
	}

	// 点赞排行
	public void learnPraise(Integer totalNum)
	{
		// -----------点赞排行-------------------------
		String hql = new QueryHelper(Learn.class, "")// l别名
				.addOrderProperty("praise", false)// 按点赞排行降序
				.getListQueryHql();
		List<Learn> learns = learnService.getByHql(hql);

		if (learns.size() <= totalNum)// 前台显示前totalNum条
		{
			ActionContext.getContext().put("learnPraise", learns);
		} else
		{
			ActionContext.getContext().put("learnPraise", learns.subList(0, totalNum));

		}
	}

	public void buildNewLearn(Integer totalNum)
	{
		// -----------最新文章-------------------------
		String hql = new QueryHelper(Learn.class, "")// l别名
				.addOrderProperty("uploadTime", false)// 按日期降序
				.getListQueryHql();
		List<Learn> learns = learnService.getByHql(hql); // 日期降序集合

		if (learns.size() <= totalNum)// 前台显示前totalNum条
		{
			ActionContext.getContext().put("learnnew", learns);
		} else
		{
			ActionContext.getContext().put("learnnew", learns.subList(0, totalNum));

		}
	}

	// ========================上传文件=============================
	protected static final long serialVersionUID = 1L;
	// 用户上传的文件
	protected File uploadFile;
	// 上传文件的文件名
	protected String uploadFileFileName;
	// 上传文件的类型
	protected String uploadFileContentType;

	protected String location = "attached/life";// 默认上传路径

	// uploadfile 属性getter方法
	public File getUploadFile()
	{
		return uploadFile;
	}

	// uploadfile属性setter方法
	public void setUploadFile(File uploadFile)
	{
		this.uploadFile = uploadFile;
	}

	// uploadfilefilename属性getter方法
	public String getUploadFileFileName()
	{
		return uploadFileFileName;
	}

	// uploadfilefilename属性setter方法
	public void setUploadFileFileName(String uploadFileFileName)
	{
		this.uploadFileFileName = uploadFileFileName;
	}

	// uploadfilecontenttype属性getter方法
	public String getUploadFileContentType()
	{
		return uploadFileContentType;
	}

	// uploadfilecontenttype属性setter方法
	public void setUploadFileContentType(String uploadFileContentType)
	{
		this.uploadFileContentType = uploadFileContentType;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public void upfile() throws Exception
	{
		if (uploadFile != null)
		{
			// 上传文件存放的目录
			String dataDir = ServletActionContext.getServletContext().getRealPath(location);
			// 上传文件在服务器具体的位置
			File savedFile = new File(dataDir, uploadFileFileName);
			// 将上传文件从临时文件复制到指定文件
			uploadFile.renameTo(savedFile);
		}
	}

	public List<Message> messages(Integer total)
	{
		String hqlString = "FROM Message ORDER BY meaageTime DESC";
		List<Message> messages = messageService.getByHql(hqlString);
		if (messages.size() <= total)
		{
			return messages;
		} else
		{
			List<Message> messages2 = new ArrayList<Message>();
			int i = 0;
			while (i < total)
			{
				messages2.add(messages.get(i++));
			}
			return messages2;
		}

	}

	// 获取留言随机条数
	public HashSet<Message> roundMessages(Integer total)
	{
		HashSet<Message> messageHashSet = new HashSet<Message>();
		ArrayList<Message> messages=new ArrayList<Message>();
		List<Message> message = messageService.findAll();
		for (int i = 0; i < message.size(); i++)
		{
			boolean sign=true;
			String qqname=message.get(i).getMessageName();
			for (Message message2 : messages)
			{
				if (message2.getMessageName().equals(qqname))
				{
					sign=false;
				}
			}
			if (sign)
			{
				messages.add(message.get(i));
			}
			
		}
		if (messages.size() <= total)
		{
			messageHashSet.addAll(messages);
			return messageHashSet;
		} else
		{

			while (true)
			{
				int i = (int) (Math.random() * messages.size());
				messageHashSet.add(messages.get(i));
				if (messageHashSet.size() == total)
				{
					break;
				}

			}
			return messageHashSet;
		}
	}

}
