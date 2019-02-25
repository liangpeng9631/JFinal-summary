## 关于JFinal事务
- 使用@Before(Tx.class)方式管理事务时需要增强AOP否则事务不生效，即Enhancer.enhance()
```
public DemoService {
  @Before(Tx.class)
  public void test() throws Exception {
    // TODO
  }
}
public DemoController {
  private DemoService demoService = Enhancer.enhance(DemoService.class)
  
  public void test() throws Exception {
    demoService.test()
  }
}
```
