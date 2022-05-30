package provider.mapper;

import com.github.pagehelper.Page;
import common.entity.Member;

import java.util.List;

public interface MemberMapper {

  /**
   * 查询表 t_member 的所有记录
   * @return
   */
  List<Member> findAll();

  /**
   * 向表 t_member 新增一条记录
   * @param member
   */
  void insert(Member member);

  /**
   * 根据id，删除表 t_member 的一条记录
   * @param id
   */
  void deleteById(Integer id);

  /**
   * 根据id，查找表 t_member 的一条记录
   * @param id
   * @return
   */
  Member findById(Integer id);

  /**
   * 根据手机号，查找表 t_member 的一条记录
   * @param telephone
   * @return
   */
  Member findByTelephone(String telephone);

  /**
   * 根据查询条件，查找表 t_member 的相关记录
   * @param queryString
   * @return
   */
  Page<Member> findByCondition(String queryString);

  /**
   * 更新表 t_member 的一条记录
   * @param member
   */
  void update(Member member);

  /**
   * 统计在指定日期之前的会员数量
   * @param date
   * @return
   */
  Long findCountBeforeDate(String date);

  /**
   * 统计在指定日期当天成为会员的数量
   * @param date
   * @return
   */
  Long findCountByDate(String date);

  /**
   * 统计在指定日期之后的会员数量
   * @param date
   * @return
   */
  Long findCountAfterDate(String date);

  /**
   * 统计会员总数量
   * @return
   */
  Long findTotal();

}
