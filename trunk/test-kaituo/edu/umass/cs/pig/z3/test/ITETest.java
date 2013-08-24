package edu.umass.cs.pig.z3.test;


import edu.umass.cs.pig.MainConfig;
import edu.umass.cs.pig.z3.DisplayModel;
import edu.umass.cs.pig.z3.PatternArrayTheirs;
import edu.umass.cs.pig.z3.SymbolArrayTheirs;
import edu.umass.cs.z3.AstArray;
import edu.umass.cs.z3.AstArrayTheirs;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_ast;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_config;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_func_decl;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_model;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_pattern;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_sort;
import edu.umass.cs.z3.SortArray;
import edu.umass.cs.z3.Z3;


/**
 * 01772 {
	01773     Z3_context ctx;
	01774     Z3_ast f, one, zero, ite;
	01775     
	01776     printf("\nite_example\n");
	01777     LOG_MSG("ite_example");
	01778 
	01779     ctx = mk_context();
	01780     
	01781     f    = Z3_mk_false(ctx);
	01782     one  = mk_int(ctx, 1);
	01783     zero = mk_int(ctx, 0);
	01784     ite  = Z3_mk_ite(ctx, f, one, zero);
	01785     
	01786     printf("term: %s\n", Z3_ast_to_string(ctx, ite));
	01787 
	01788     // delete logical context
	01789     Z3_del_context(ctx);
	01790 }
 * @author kaituo
 *
 */
public class ITETest extends Z3PlainTest {
	public void testIte_example() {
		System.out.println("------------------testIte_example()---------------------");
		SWIGTYPE_p__Z3_sort int_sort;
		SWIGTYPE_p__Z3_ast f, one, zero, ite;
		
		System.out.println("ite_example\n");
		
		z3.push();
		f = z3.mk_false();
		int_sort = z3.mk_int_sort();
		one = z3.mk_int(1, int_sort);
		zero = z3.mk_int(0, int_sort);
		ite = z3.mk_ite(f, one, zero);
		
		System.out.println("term : " + z3.ast_to_string(ite));
		z3.pop(1);
	}
	
	public void testIte_example2() {
		System.out.println("------------------testIte_example2()---------------------");
		z3.push();
		SWIGTYPE_p__Z3_sort[] domain = new SWIGTYPE_p__Z3_sort[3];
		SWIGTYPE_p__Z3_sort intT = z3.mk_int_sort();
		SWIGTYPE_p__Z3_sort boolT = z3.mk_bool_sort();
        domain[0] = intT;  
        domain[1] = intT;          
        domain[2] = intT;  
        SWIGTYPE_p__Z3_func_decl FPolicy = z3.mk_func_decl2(z3.mk_string_symbol("FPolicy"), boolT, domain);      

        SWIGTYPE_p__Z3_ast[] args = new SWIGTYPE_p__Z3_ast[3];
        args[0] = z3.mk_numeral("0", intT);
        args[1] = z3.mk_numeral("1", intT);
        args[2] = z3.mk_numeral("30", intT);
        z3.assert_cnstr(z3.mk_app(FPolicy, args));

        args[1] = z3.mk_numeral("2", intT);
        args[2] = z3.mk_numeral("20", intT);
        z3.assert_cnstr(z3.mk_app(FPolicy, args));
        
        SWIGTYPE_p__Z3_model model = z3.check_and_get_model_simple();
        
        if (model!=null) {
        	DisplayModel d = new DisplayModel();
        	d.display_model(model);
        }
        z3.pop(1);
	}
	
	public void testIte_example3() {
		System.out.println("------------------testIte_example3()---------------------");
		z3.push();
		SWIGTYPE_p__Z3_sort[] domain = new SWIGTYPE_p__Z3_sort[3];
		SWIGTYPE_p__Z3_sort intT = z3.mk_int_sort();
		SWIGTYPE_p__Z3_sort boolT = z3.mk_bool_sort();
        domain[0] = intT;  
        domain[1] = intT;          
        domain[2] = intT;  
        SWIGTYPE_p__Z3_func_decl FPolicy = z3.mk_func_decl2(z3.mk_string_symbol("FPolicy"), boolT, domain);      

        SWIGTYPE_p__Z3_ast[] args = new SWIGTYPE_p__Z3_ast[3];
        args[0] = z3.mk_numeral("0", intT);
        args[1] = z3.mk_numeral("1", intT);
        args[2] = z3.mk_numeral("30", intT);
 //       SWIGTYPE_p__Z3_ast case1 = z3.mk_app(FPolicy, args);

        SWIGTYPE_p__Z3_ast[] args2 = new SWIGTYPE_p__Z3_ast[3];
        args2[0] = z3.mk_numeral("0", intT);
        args2[1] = z3.mk_numeral("2", intT);
        args2[2] = z3.mk_numeral("20", intT);
 //       SWIGTYPE_p__Z3_ast case2 = z3.mk_app(FPolicy, args);
        
        SWIGTYPE_p__Z3_ast[] func = new SWIGTYPE_p__Z3_ast[3];
        func[0] = z3.mk_var("x!1", intT);
        func[1] = z3.mk_var("x!2", intT);
        func[2] = z3.mk_var("x!3", intT);
        SWIGTYPE_p__Z3_ast fun = z3.mk_app(FPolicy, func);

        
        SWIGTYPE_p__Z3_ast ite1, f, t, ite2, condition1, condition1_1, condition1_2, condition1_3, condition2, condition2_1, condition2_2, condition2_3;
        f = z3.mk_false();
        t = z3.mk_true();
        condition1_1 = z3.mk_eq(func[0], args[0]);
        condition1_2 = z3.mk_eq(func[1], args[1]);
        condition1_3 = z3.mk_eq(func[2], args[2]);
        
        condition2_1 = z3.mk_eq(func[0], args2[0]);
        condition2_2 = z3.mk_eq(func[1], args2[1]);
        condition2_3 = z3.mk_eq(func[2], args2[2]);
        
        AstArray andArgs1 = new AstArrayTheirs(3);
        andArgs1.setitem(0, condition1_1);
        andArgs1.setitem(1, condition1_2);
        andArgs1.setitem(2, condition1_3);
        
        AstArray andArgs2 = new AstArrayTheirs(3);
        andArgs2.setitem(0, condition2_1);
        andArgs2.setitem(1, condition2_2);
        andArgs2.setitem(2, condition2_3);
        
        condition1 = z3.mk_and(3, andArgs1.cast());
        condition2 = z3.mk_and(3, andArgs2.cast());
        ite2 = z3.mk_ite(condition2, z3.mk_eq(fun, t), z3.mk_eq(fun, f));
        ite1 = z3.mk_ite(condition1,z3.mk_eq(fun, t) ,ite2);
        System.out.println("term : " + z3.ast_to_string(ite1));
        
        z3.assert_cnstr(ite1);
        SWIGTYPE_p__Z3_model model = z3.check_and_get_model_simple();
        
        if (model!=null) {
        	DisplayModel d = new DisplayModel();
        	d.display_model(model);
        }
        z3.pop(1);
	}
	
	public void testIte_example4() {
		System.out.println("------------------testIte_example4()---------------------");
		z3.push();
		SWIGTYPE_p__Z3_sort[] domain = new SWIGTYPE_p__Z3_sort[3];
		SWIGTYPE_p__Z3_sort intT = z3.mk_int_sort();
        domain[0] = intT;  
        domain[1] = intT;          
        domain[2] = intT;  
        SWIGTYPE_p__Z3_func_decl FPolicy = z3.mk_func_decl2(z3.mk_string_symbol("FPolicy"), intT, domain);      

        SWIGTYPE_p__Z3_ast[] args = new SWIGTYPE_p__Z3_ast[3];
        args[0] = z3.mk_numeral("0", intT);
        args[1] = z3.mk_numeral("1", intT);
        args[2] = z3.mk_numeral("30", intT);
//        SWIGTYPE_p__Z3_ast case1 = z3.mk_app(FPolicy, args);

        SWIGTYPE_p__Z3_ast[] args2 = new SWIGTYPE_p__Z3_ast[3];
        args2[0] = z3.mk_numeral("0", intT);
        args2[1] = z3.mk_numeral("2", intT);
        args2[2] = z3.mk_numeral("20", intT);
//       SWIGTYPE_p__Z3_ast case2 = z3.mk_app(FPolicy, args);
        
        SWIGTYPE_p__Z3_ast[] func = new SWIGTYPE_p__Z3_ast[3];
        func[0] = z3.mk_var("x!1", intT);
        func[1] = z3.mk_var("x!2", intT);
        func[2] = z3.mk_var("x!3", intT);
        SWIGTYPE_p__Z3_ast fun = z3.mk_app(FPolicy, func);
        
        SWIGTYPE_p__Z3_ast ite1, f1, f2, f, ite2, condition1, condition1_1, condition1_2, condition1_3, condition2, condition2_1, condition2_2, condition2_3;
        f1 = z3.mk_numeral("567", intT);
        f = z3.mk_numeral("0", intT);
        f2 = z3.mk_numeral("544", intT);
        condition1_1 = z3.mk_eq(func[0], args[0]);
        condition1_2 = z3.mk_eq(func[1], args[1]);
        condition1_3 = z3.mk_eq(func[2], args[2]);
        
        condition2_1 = z3.mk_eq(func[0], args2[0]);
        condition2_2 = z3.mk_eq(func[1], args2[1]);
        condition2_3 = z3.mk_eq(func[2], args2[2]);
        
        AstArray andArgs1 = new AstArrayTheirs(3);
        andArgs1.setitem(0, condition1_1);
        andArgs1.setitem(1, condition1_2);
        andArgs1.setitem(2, condition1_3);
        
        AstArray andArgs2 = new AstArrayTheirs(3);
        andArgs2.setitem(0, condition2_1);
        andArgs2.setitem(1, condition2_2);
        andArgs2.setitem(2, condition2_3);
        
        condition1 = z3.mk_and(3, andArgs1.cast());
        condition2 = z3.mk_and(3, andArgs2.cast());
        ite2 = z3.mk_ite(condition2, z3.mk_eq(fun, f2), z3.mk_eq(fun, f));
        ite1 = z3.mk_ite(condition1,z3.mk_eq(fun, f1) ,ite2);
        System.out.println("term : " + z3.ast_to_string(ite1));
        
        SWIGTYPE_p__Z3_ast z = z3.mk_var("z", intT);
        SWIGTYPE_p__Z3_ast[] addition = new SWIGTYPE_p__Z3_ast[2];
        addition[0] = z;
        addition[1] = fun;
        SWIGTYPE_p__Z3_ast cntr1 = z3.mk_eq(fun, z3.mk_add(2, addition));
        SWIGTYPE_p__Z3_ast cntr2 = z3.mk_not(z3.mk_eq(fun, f));
        
        z3.assert_cnstr(ite1);
        z3.assert_cnstr(cntr2);
        z3.assert_cnstr(cntr1);
        SWIGTYPE_p__Z3_model model = z3.check_and_get_model_simple();
        
        if (model!=null) {
        	DisplayModel d = new DisplayModel();
        	d.display_model(model);
        }
        z3.pop(1);
	}
	
	public void testIte_example5() {
		System.out.println("------------------testIte_example5()---------------------");
		z3.push();
		SWIGTYPE_p__Z3_sort[] domain = new SWIGTYPE_p__Z3_sort[3];
		SWIGTYPE_p__Z3_sort intT = z3.mk_int_sort();
        domain[0] = intT;  
        domain[1] = intT;          
        domain[2] = intT;  
        SWIGTYPE_p__Z3_func_decl FPolicy = z3.mk_func_decl2(z3.mk_string_symbol("FPolicy"), intT, domain);      

        SWIGTYPE_p__Z3_ast[] args = new SWIGTYPE_p__Z3_ast[3];
        args[0] = z3.mk_numeral("0", intT);
        args[1] = z3.mk_numeral("1", intT);
        args[2] = z3.mk_numeral("30", intT);
//        SWIGTYPE_p__Z3_ast case1 = z3.mk_app(FPolicy, args);

        SWIGTYPE_p__Z3_ast[] args2 = new SWIGTYPE_p__Z3_ast[3];
        args2[0] = z3.mk_numeral("0", intT);
        args2[1] = z3.mk_numeral("2", intT);
        args2[2] = z3.mk_numeral("20", intT);
//       SWIGTYPE_p__Z3_ast case2 = z3.mk_app(FPolicy, args);
        
        SWIGTYPE_p__Z3_ast[] func = new SWIGTYPE_p__Z3_ast[3];
        func[0] = z3.mk_var("x!1", intT);
        func[1] = z3.mk_var("x!2", intT);
        func[2] = z3.mk_var("x!3", intT);
        SWIGTYPE_p__Z3_ast fun = z3.mk_app(FPolicy, func);
        
        SWIGTYPE_p__Z3_ast ite1, f1, f2, f, ite2, condition1, condition1_1, condition1_2, condition1_3, condition2, condition2_1, condition2_2, condition2_3;
        f1 = z3.mk_numeral("567", intT);
        f = z3.mk_numeral("0", intT);
        f2 = z3.mk_numeral("544", intT);
        condition1_1 = z3.mk_eq(func[0], args[0]);
        condition1_2 = z3.mk_eq(func[1], args[1]);
        condition1_3 = z3.mk_eq(func[2], args[2]);
        
        condition2_1 = z3.mk_eq(func[0], args2[0]);
        condition2_2 = z3.mk_eq(func[1], args2[1]);
        condition2_3 = z3.mk_eq(func[2], args2[2]);
        
        AstArray andArgs1 = new AstArrayTheirs(3);
        andArgs1.setitem(0, condition1_1);
        andArgs1.setitem(1, condition1_2);
        andArgs1.setitem(2, condition1_3);
        
        AstArray andArgs2 = new AstArrayTheirs(3);
        andArgs2.setitem(0, condition2_1);
        andArgs2.setitem(1, condition2_2);
        andArgs2.setitem(2, condition2_3);
        
        condition1 = z3.mk_and(3, andArgs1.cast());
        condition2 = z3.mk_and(3, andArgs2.cast());
        ite2 = z3.mk_ite(condition2, z3.mk_eq(fun, f2), z3.mk_eq(fun, f));
        ite1 = z3.mk_ite(condition1,z3.mk_eq(fun, f1) ,ite2);
        System.out.println("term : " + z3.ast_to_string(ite1));
        
        SWIGTYPE_p__Z3_ast z = z3.mk_var("z", intT);
        SWIGTYPE_p__Z3_ast[] addition = new SWIGTYPE_p__Z3_ast[2];
        addition[0] = z;
        addition[1] = fun;
        SWIGTYPE_p__Z3_ast cntr1 = z3.mk_eq(fun, z3.mk_add(2, addition));
        SWIGTYPE_p__Z3_ast cntr2 = z3.mk_not(z3.mk_eq(fun, f));
        
        z3.assert_cnstr(ite1);
        z3.assert_cnstr(cntr2);
        z3.assert_cnstr(cntr1);
        SWIGTYPE_p__Z3_model model = z3.check_and_get_model_simple();
        
        if (model!=null) {
        	DisplayModel d = new DisplayModel();
        	d.display_model(model);
        } else {
        	System.out.println("unsat");
        }
        z3.pop(1);
	}
	
	public void testIte_example6() {
		System.out.println("------------------testIte_example6()---------------------");
		z3.push();
		SWIGTYPE_p__Z3_sort[] domain = new SWIGTYPE_p__Z3_sort[3];
		SWIGTYPE_p__Z3_sort intT = z3.mk_int_sort();
        domain[0] = intT;  
        domain[1] = intT;          
        domain[2] = intT;  
        SWIGTYPE_p__Z3_func_decl FPolicy = z3.mk_func_decl2(z3.mk_string_symbol("FPolicy"), intT, domain);      

        SWIGTYPE_p__Z3_ast[] args = new SWIGTYPE_p__Z3_ast[3];
        args[0] = z3.mk_numeral("0", intT);
        args[1] = z3.mk_numeral("1", intT);
        args[2] = z3.mk_numeral("30", intT);
        SWIGTYPE_p__Z3_ast case1 = z3.mk_app(FPolicy, args);

        SWIGTYPE_p__Z3_ast[] args2 = new SWIGTYPE_p__Z3_ast[3];
        args2[0] = z3.mk_numeral("0", intT);
        args2[1] = z3.mk_numeral("2", intT);
        args2[2] = z3.mk_numeral("20", intT);
       SWIGTYPE_p__Z3_ast case2 = z3.mk_app(FPolicy, args);
        
        SWIGTYPE_p__Z3_ast[] func = new SWIGTYPE_p__Z3_ast[3];
        func[0] = z3.mk_var("x!1", intT);
        func[1] = z3.mk_var("x!2", intT);
        func[2] = z3.mk_var("x!3", intT);
        SWIGTYPE_p__Z3_ast fun = z3.mk_app(FPolicy, func);
        
        SWIGTYPE_p__Z3_ast ite1, f1, f2, f, ite2, condition1, condition1_1, condition1_2, condition1_3, condition2, condition2_1, condition2_2, condition2_3;
        f1 = z3.mk_numeral("567", intT);
        f = z3.mk_numeral("0", intT);
        f2 = z3.mk_numeral("544", intT);
        condition1_1 = z3.mk_eq(func[0], args[0]);
        condition1_2 = z3.mk_eq(func[1], args[1]);
        condition1_3 = z3.mk_eq(func[2], args[2]);
        
        condition2_1 = z3.mk_eq(func[0], args2[0]);
        condition2_2 = z3.mk_eq(func[1], args2[1]);
        condition2_3 = z3.mk_eq(func[2], args2[2]);
        
        AstArray andArgs1 = new AstArrayTheirs(3);
        andArgs1.setitem(0, condition1_1);
        andArgs1.setitem(1, condition1_2);
        andArgs1.setitem(2, condition1_3);
        
        AstArray andArgs2 = new AstArrayTheirs(3);
        andArgs2.setitem(0, condition2_1);
        andArgs2.setitem(1, condition2_2);
        andArgs2.setitem(2, condition2_3);
        
        condition1 = z3.mk_and(3, andArgs1.cast());
        condition2 = z3.mk_and(3, andArgs2.cast());
        ite2 = z3.mk_ite(condition2, f2, f);
        ite1 = z3.mk_ite(condition1, f1 ,ite2);
        System.out.println("term : " + z3.ast_to_string(ite1));
        
        
        SWIGTYPE_p__Z3_ast funExpr = z3.mk_eq(
        	      ite1, fun);
        SWIGTYPE_p__Z3_ast[] bounds = new SWIGTYPE_p__Z3_ast[3];
        bounds[0] = z3.mk_bound(0, intT);
        bounds[1] = z3.mk_bound(1, intT);
        bounds[2] = z3.mk_bound(2, intT);
        SWIGTYPE_p__Z3_ast funPattern = z3.mk_app(FPolicy, bounds);
        AstArrayTheirs ast = new AstArrayTheirs(1);
        ast.setitem(0, funPattern);
//        SWIGTYPE_p__Z3_ast imp = z3.mk_implies(funExpr, funPattern);
        
        SWIGTYPE_p__Z3_pattern p = z3.mk_pattern(1, ast.cast());
        PatternArrayTheirs pattern = new PatternArrayTheirs(1);
        pattern.setitem(0, p);
        SortArray sort = new SortArray(3);
        sort.setitem(0, intT);
        sort.setitem(1, intT);
        sort.setitem(2, intT);
        SymbolArrayTheirs symbol = new SymbolArrayTheirs(3);
        symbol.setitem(0, z3.mk_string_symbol("x!1"));
        symbol.setitem(1, z3.mk_string_symbol("x!2"));
        symbol.setitem(2, z3.mk_string_symbol("x!3"));
        SWIGTYPE_p__Z3_ast q           = z3.mk_forall( 
        				0L, /* using default weight */
        	                                  1L, /* number of patterns */
        	                                  pattern.cast(), /* address of the "array" of patterns */
        	                                  3L, /* number of quantified variables */
        	                                  sort.cast(), 
        	                                  symbol.cast(),
                                              funExpr);

        
        
        SWIGTYPE_p__Z3_ast z = z3.mk_var("z", intT);
        SWIGTYPE_p__Z3_ast[] addition = new SWIGTYPE_p__Z3_ast[2];
        addition[0] = z;
        addition[1] = fun;
//        SWIGTYPE_p__Z3_ast cntr1 = z3.mk_eq(fun, func[0]);
//        SWIGTYPE_p__Z3_ast cntr2 = z3.mk_not(z3.mk_eq(fun, f));
        
        z3.assert_cnstr(q);
//        z3.assert_cnstr(cntr2);
//        z3.assert_cnstr(cntr1);
        z3.assert_cnstr(z3.mk_eq(case1, f1));
        z3.assert_cnstr(z3.mk_eq(case2, f2));
        SWIGTYPE_p__Z3_model model = z3.check_and_get_model_simple();
        
        if (model!=null) {
        	DisplayModel d = new DisplayModel();
        	d.display_model(model);
        } else {
        	System.out.println("unsat");
        }
        z3.pop(1);
	}

	/**
	 * http://stackoverflow.com/questions/11247083/how-to-define-fun-in-z3-fixedpoint-by-using-c-sharp-api
	 * 
	 * 

		Q: I want to define functions that can be used in the fixedpoint rule. For example:
		
		(declare-var int1 Int)
		(declare-var int2 Int)
		(declare-rel phi( Int Int))
		(define-fun init((a Int)(b Int)) Bool
		    (and
		        (= a 0)
		        (= b 0)
		    )
		)
		
		(rule ( =>
		    (init int1 int2)
		    (phi int1 int2))
		)
		
		(query (and (phi int1 int2) (= int1 0)))   
		
		It is said that there is no api for "define-fun", it should be translated into quantifier in API. I try to use c# api to implement it. However, I get the wrong answer. ( the result should be satisfiable, however, it is unsatisfiable) the code:
		
		using (Context ctx = new Context())
		{
		    var s = ctx.MkFixedpoint();
		    Solver slover = ctx.MkSolver();
		    IntSort B = ctx.IntSort;
		    RealSort R = ctx.RealSort;
		    BoolSort T = ctx.BoolSort;
		    IntExpr int1 = (IntExpr) ctx.MkBound(0, B);
		    IntExpr int2 = (IntExpr) ctx.MkBound(1, B);
		    FuncDecl phi = ctx.MkFuncDecl("phi", new Sort[] {B, B }, T);
		    FuncDecl init = ctx.MkFuncDecl("init", new Sort[] {B, B}, T);
		    s.RegisterRelation(phi);
		    s.RegisterRelation(init);
		    Expr[] initBound = new Expr[2];
		    initBound[0] = ctx.MkConst("init" + 0, init.Domain[0]);
		    initBound[1] = ctx.MkConst("init" + 1, init.Domain[1]);
		    Expr initExpr = ctx.MkEq((BoolExpr)init[initBound],
		    ctx.MkAnd(ctx.MkEq(initBound[0], ctx.MkInt(0)), ctx.MkEq(initBound[1], ctx.MkInt(0))));
		    Quantifier q = ctx.MkForall(initBound, initExpr, 1);
		    slover.Assert(q);
		    s.AddRule(ctx.MkImplies((BoolExpr)init[int1, int2],
		    (BoolExpr)phi[int1, int2]));
		    Status a = s.Query(ctx.MkAnd((BoolExpr)phi[int1,int2],ctx.MkEq(int1, ctx.MkInt(0))));
		}
		
		What's the problem?
		
		A:
		Below follows a version where the macro is turned into a rule.

		var s = ctx.MkFixedpoint();
		IntSort B = ctx.IntSort;
		BoolSort T = ctx.BoolSort;
		IntExpr int1 = (IntExpr) ctx.MkBound(0, B);
		IntExpr int2 = (IntExpr) ctx.MkBound(1, B);
		FuncDecl phi = ctx.MkFuncDecl("phi", new Sort[] {B, B }, T);
		FuncDecl init = ctx.MkFuncDecl("init", new Sort[] {B, B}, T);
		s.RegisterRelation(phi);
		s.RegisterRelation(init);
		Expr[] initBound = new Expr[2];
		initBound[0] = ctx.MkConst("init" + 0, init.Domain[0]);
		initBound[1] = ctx.MkConst("init" + 1, init.Domain[1]);
		Expr initExpr = ctx.MkImplies(
		      ctx.MkAnd(ctx.MkEq(initBound[0], ctx.MkInt(0)), ctx.MkEq(initBound[1], ctx.MkInt(0))),
		              (BoolExpr)init[initBound]);
		
		Quantifier q = ctx.MkForall(initBound, initExpr, 1);
		
		s.AddRule(q);
		s.AddRule(ctx.MkImplies((BoolExpr)init[int1, int2], (BoolExpr)phi[int1, int2]));
		
		Status a = s.Query(ctx.MkAnd((BoolExpr)phi[int1,int2],ctx.MkEq(int1, ctx.MkInt(0))));
		
		Console.WriteLine("{0}",a);
		
		Console.WriteLine("{0}", s.GetAnswer());
		
		Alternatively one can write a function
		
		Term init(Context ctx, Term a, Term b) { 
		   Term zero = ctx.MkInt(0);
		   return ctx.MkAnd(ctx.MkEq(a,zero),ctx.MkEq(b,zero));
		}
		
		and use this function where you apply "init".
		
		
		Also from Z3's C API example:
		http://research.microsoft.com/en-us/um/redmond/projects/z3/test__capi_8c_source.html#l01099
		
		00278 void assert_inj_axiom(Z3_context ctx, Z3_func_decl f, unsigned i) 
		00279 {
		00280     unsigned sz, j;
		00281     Z3_sort finv_domain, finv_range;
		00282     Z3_func_decl finv;
		00283     Z3_sort * types; // types of the quantified variables /
		00284     Z3_symbol *   names; // names of the quantified variables /
		00285     Z3_ast * xs;         // arguments for the application f(x_0, ..., x_i, ..., x_{n-1}) /
		00286     Z3_ast   x_i, fxs, finv_fxs, eq;
		00287     Z3_pattern p;
		00288     Z3_ast   q;
		00289     sz = Z3_get_domain_size(ctx, f);
		00290 
		00291     if (i >= sz) {
		00292         exitf("failed to create inj axiom");
		00293     }
		00294     
		00295     // declare the i-th inverse of f: finv /
		00296     finv_domain = Z3_get_range(ctx, f);
		00297     finv_range  = Z3_get_domain(ctx, f, i);
		00298     finv        = Z3_mk_fresh_func_decl(ctx, "inv", 1, &finv_domain, finv_range);
		00299 
		00300     // allocate temporary arrays /
		00301     types       = (Z3_sort *) malloc(sizeof(Z3_sort) * sz);
		00302     names       = (Z3_symbol *) malloc(sizeof(Z3_symbol) * sz);
		00303     xs          = (Z3_ast *) malloc(sizeof(Z3_ast) * sz);
		00304     
		00305     // fill types, names and xs 
		00306     for (j = 0; j < sz; j++) { types[j] = Z3_get_domain(ctx, f, j); };
		00307     for (j = 0; j < sz; j++) { names[j] = Z3_mk_int_symbol(ctx, j); };
		00308     for (j = 0; j < sz; j++) { xs[j]    = Z3_mk_bound(ctx, j, types[j]); };
		00309 
		00310     x_i = xs[i];
		00311 
		00312     // create f(x_0, ..., x_i, ..., x_{n-1})  
		00313     fxs         = Z3_mk_app(ctx, f, sz, xs);
		00314 
		00315     // create f_inv(f(x_0, ..., x_i, ..., x_{n-1})) /
		00316     finv_fxs    = mk_unary_app(ctx, finv, fxs);
		00317 
		00318     // create finv(f(x_0, ..., x_i, ..., x_{n-1})) = x_i /
		00319     eq          = Z3_mk_eq(ctx, finv_fxs, x_i);
		00320 
		00321     // use f(x_0, ..., x_i, ..., x_{n-1}) as the pattern for the quantifier 
		00322     p           = Z3_mk_pattern(ctx, 1, &fxs);
		00323     printf("pattern: %s\n", Z3_pattern_to_string(ctx, p));
		00324     printf("\n");
		00325 
		00326     // create & assert quantifier 
		00327     q           = Z3_mk_forall(ctx, 
		00328                                  0, // using default weight
		00329                                  1, // number of patterns 
		00330                                  &p, // address of the "array" of patterns 
		00331                                  sz, // number of quantified variables 
		00332                                  types, 
		00333                                  names,
		00334                                  eq);
		00335     printf("assert axiom:\n%s\n", Z3_ast_to_string(ctx, q));
		00336     Z3_assert_cnstr(ctx, q);
		00337 
		00338     // free temporary arrays 
		00339     free(types);
		00340     free(names);
		00341     free(xs);
		00342 }


	 */
	public void testIte_example7() {
		System.out.println("------------------testIte_example7()---------------------");
		z3.push();
//		SWIGTYPE_p__Z3_config config = MainConfig.get();
//	    Z3.Z3_set_param_value(config, "MBQI", "false");
//	    Z3.Z3_set_param_value(config, "MACRO_FINDER", "true");
	    
		SWIGTYPE_p__Z3_sort[] domain = new SWIGTYPE_p__Z3_sort[3];
		SWIGTYPE_p__Z3_sort intT = z3.mk_int_sort();
        domain[0] = intT;  
        domain[1] = intT;          
        domain[2] = intT;  
        SWIGTYPE_p__Z3_func_decl FPolicy = z3.mk_func_decl2(z3.mk_string_symbol("FPolicy"), intT, domain);      

        SWIGTYPE_p__Z3_ast[] args = new SWIGTYPE_p__Z3_ast[3];
        args[0] = z3.mk_numeral("0", intT);
        args[1] = z3.mk_numeral("1", intT);
        args[2] = z3.mk_numeral("30", intT);
        SWIGTYPE_p__Z3_ast case1 = z3.mk_app(FPolicy, args);

        SWIGTYPE_p__Z3_ast[] args2 = new SWIGTYPE_p__Z3_ast[3];
        args2[0] = z3.mk_numeral("0", intT);
        args2[1] = z3.mk_numeral("2", intT);
        args2[2] = z3.mk_numeral("20", intT);
       SWIGTYPE_p__Z3_ast case2 = z3.mk_app(FPolicy, args);
        
       SWIGTYPE_p__Z3_ast[] bounds = new SWIGTYPE_p__Z3_ast[3];
       bounds[0] = z3.mk_bound(0, intT);
       bounds[1] = z3.mk_bound(1, intT);
       bounds[2] = z3.mk_bound(2, intT);
//        SWIGTYPE_p__Z3_ast[] func = new SWIGTYPE_p__Z3_ast[3];
//        func[0] = z3.mk_var("x!1", intT);
//        func[1] = z3.mk_var("x!2", intT);
//        func[2] = z3.mk_var("x!3", intT);
        SWIGTYPE_p__Z3_ast fun = z3.mk_app(FPolicy, bounds);
        
        SWIGTYPE_p__Z3_ast ite1, f1, f2, f, ite2, condition1, condition1_1, condition1_2, condition1_3, condition2, condition2_1, condition2_2, condition2_3;
        f1 = z3.mk_numeral("567", intT);
        f = z3.mk_numeral("0", intT);
        f2 = z3.mk_numeral("544", intT);
        condition1_1 = z3.mk_eq(bounds[0], args[0]);
        condition1_2 = z3.mk_eq(bounds[1], args[1]);
        condition1_3 = z3.mk_eq(bounds[2], args[2]);
        
        condition2_1 = z3.mk_eq(bounds[0], args2[0]);
        condition2_2 = z3.mk_eq(bounds[1], args2[1]);
        condition2_3 = z3.mk_eq(bounds[2], args2[2]);
        
        AstArray andArgs1 = new AstArrayTheirs(3);
        andArgs1.setitem(0, condition1_1);
        andArgs1.setitem(1, condition1_2);
        andArgs1.setitem(2, condition1_3);
        
        AstArray andArgs2 = new AstArrayTheirs(3);
        andArgs2.setitem(0, condition2_1);
        andArgs2.setitem(1, condition2_2);
        andArgs2.setitem(2, condition2_3);
        
        condition1 = z3.mk_and(3, andArgs1.cast());
        condition2 = z3.mk_and(3, andArgs2.cast());
        ite2 = z3.mk_ite(condition2, z3.mk_eq(fun, f2), z3.mk_eq(fun, f));
        ite1 = z3.mk_ite(condition1,z3.mk_eq(fun, f1) ,ite2);
        System.out.println("term : " + z3.ast_to_string(ite1));
        
        
//        SWIGTYPE_p__Z3_ast funExpr = z3.mk_eq(
//        	      ite1, fun);
        
//        SWIGTYPE_p__Z3_ast funPattern = z3.mk_app(FPolicy, bounds);
        AstArrayTheirs ast = new AstArrayTheirs(1);
        ast.setitem(0, fun);
//        SWIGTYPE_p__Z3_ast imp = z3.mk_implies(funExpr, funPattern);
        
        SWIGTYPE_p__Z3_pattern p = z3.mk_pattern(1, ast.cast());
        PatternArrayTheirs pattern = new PatternArrayTheirs(1);
        pattern.setitem(0, p);
        SortArray sort = new SortArray(3);
        sort.setitem(0, intT);
        sort.setitem(1, intT);
        sort.setitem(2, intT);
        SymbolArrayTheirs symbol = new SymbolArrayTheirs(3);
        symbol.setitem(0, z3.mk_int_symbol(0));
        symbol.setitem(1, z3.mk_int_symbol(1));
        symbol.setitem(2, z3.mk_int_symbol(2));
        SWIGTYPE_p__Z3_ast q           = z3.mk_forall( 
        				0L, /* using default weight */
        	                                  1L, /* number of patterns */
        	                                  pattern.cast(), /* address of the "array" of patterns */
        	                                  3L, /* number of quantified variables */
        	                                  sort.cast(), 
        	                                  symbol.cast(),
                                              ite1);

        
        System.out.println("quantifier : " + z3.ast_to_string(q));
        SWIGTYPE_p__Z3_ast z = z3.mk_var("z", intT);
        SWIGTYPE_p__Z3_ast[] addition = new SWIGTYPE_p__Z3_ast[2];
        addition[0] = z;
        addition[1] = fun;
        SWIGTYPE_p__Z3_ast cntr1 = z3.mk_eq(fun, z3.mk_add(2, addition));
        SWIGTYPE_p__Z3_ast cntr2 = z3.mk_not(z3.mk_eq(fun, f));
        
        z3.assert_cnstr(q);
        z3.assert_cnstr(cntr2);
        z3.assert_cnstr(cntr1);
        z3.assert_cnstr(z3.mk_eq(case1, f1));
        z3.assert_cnstr(z3.mk_eq(case2, f2));
        SWIGTYPE_p__Z3_model model = z3.check_and_get_model_simple();
        
        if (model!=null) {
        	DisplayModel d = new DisplayModel();
        	d.display_model(model);
        } else {
        	System.out.println("unsat");
        }
        z3.pop(1);
	}
	
	/**
	 *  Last week, I sent you a composition about how to do correct
		substitution in a class of constraints that may involve occurrences of
		udfs, under the assumption that some invocations of udfs (and return
		values thereof) have already been observed.  So if the predicate to
		satisfy here is: f(udf(A), Z), where A is the input tuple of the udf
		function and Z is the variables outside the udf function, my goal is
		to capture functional dependencies between Z and the return value of
		udf(A):
		
		1) if there are some previously observed values that can satisfy the
		predicate, use those predicates immediately.
		2) A \cap Z = \varnothing, do direct substitution;
		3) else, do special substitution.
		
		Note that Z is a vector of variables appear outside of udfs.  For
		example, for the predicate u = Hash(A) ^ u > x ^ 1 ^ u < w + 1 and
		A={x,y}, Z={u, w}.
		
		We could also use Z3's uninterpreted function mechanism to deal with
		the udf problem.  Basically, define the function to return the values
		we know for each case that we know about.  For all values that we
		don't know anything about, we return a distinctive value.  For any
		invocation of that udf, make sure that it goes through a variable v,
		and assert that f(v) != c  (where `c' is whatever distinctive value we
		picked.)   This would save us a lot of engineering complications since
		we offload a lot of work to Z3. Uninterpreted functions allow any
		interpretation that is consistent with the constraints over the
		function.  This is in contrast to functions belonging to the signature
		of theories where functions such as + has a fixed interpretation (it
		adds two numbers).  To illustrate the use of uninterpreted functions
		to deal with udf, let us introduce two constants x, y ranging over
		integer.   Then,   let Hash be an uninterpreted function that takes
		two arguments of sort integer and results in a value of sort integer.
		We have observed two invocations of the function Hash where Hash
		applied to (x!1=33, x!2=42) results in 567, and Hash applied to
		(x!1=47, x!2=19) is results in 544.  So we restrict Hash to return 567
		when (x!1=33, x!2=42), return 544 when (x!1=47, x!2=19) and return 0
		for everything else ('0' could be replaced with any one value that
		doesn't show up in the real results).  Note that we should assert
		Hash(x, y) != 0.  Finally we provide an encoding of constraint between
		Hash applied to x, y and x.
		
		(declare-fun x () Int) ; x is a constant
		(declare-const y Int) ; syntax sugar for (declare-fun y () Int)
		(define-fun Hash ((x!1 Int)(x!2 Int)) Int
		    (ite (and (= x!1 33) (= x!2 42)) 567
		    (ite (and (= x!1 47) (= x!2 19)) 544
		     0)))
		(assert (= (Hash x y) x))
		(assert (not (= (Hash x y) 0)))
		(check-sat)
		
		Since Z3 cannot retrieve an interpretation that makes all formulas on
		the Z3 internal stack true, Z3 returns unsat.  We could change the
		constraints as follows:
		
		(declare-fun x () Int) ; x is a constant
		(declare-const y Int) ; syntax sugar for (declare-fun y () Int)
		(declare-const z Int) ; syntax sugar for (declare-fun z () Int)
		(define-fun Hash ((x!1 Int)(x!2 Int)) Int
		    (ite (and (= x!1 33) (= x!2 42)) 567
		    (ite (and (= x!1 47) (= x!2 19)) 544
		     0)))
		(assert (not (= (Hash x y) 0)))
		(assert (= (Hash x y) (+ z x)))
		(check-sat)
		(get-model)
		
		In this case, Z3 produces a satisfying model for constants x, y, z:
		
		sat (model (define-fun y () Int 42) (define-fun x () Int 33)
		(define-fun z () Int 534) )
	 */
	public void testIte_example8() {
		System.out.println("------------------testIte_example8()---------------------");
		z3.push();
		SWIGTYPE_p__Z3_sort[] domain = new SWIGTYPE_p__Z3_sort[3];
		SWIGTYPE_p__Z3_sort intT = z3.mk_int_sort();
        domain[0] = intT;  
        domain[1] = intT;          
        domain[2] = intT;  
        SWIGTYPE_p__Z3_func_decl FPolicy = z3.mk_func_decl2(z3.mk_string_symbol("FPolicy"), intT, domain);      

        SWIGTYPE_p__Z3_ast[] args = new SWIGTYPE_p__Z3_ast[3];
        args[0] = z3.mk_numeral("0", intT);
        args[1] = z3.mk_numeral("1", intT);
        args[2] = z3.mk_numeral("30", intT);
//        SWIGTYPE_p__Z3_ast case1 = z3.mk_app(FPolicy, args);

        SWIGTYPE_p__Z3_ast[] args2 = new SWIGTYPE_p__Z3_ast[3];
        args2[0] = z3.mk_numeral("0", intT);
        args2[1] = z3.mk_numeral("2", intT);
        args2[2] = z3.mk_numeral("20", intT);
//       SWIGTYPE_p__Z3_ast case2 = z3.mk_app(FPolicy, args);
        
        SWIGTYPE_p__Z3_ast[] func = new SWIGTYPE_p__Z3_ast[3];
        func[0] = z3.mk_var("x!1", intT);
        func[1] = z3.mk_var("x!2", intT);
        func[2] = z3.mk_var("x!3", intT);
        SWIGTYPE_p__Z3_ast fun = z3.mk_app(FPolicy, func);
        
        SWIGTYPE_p__Z3_ast ite1, f1, f2, f, ite2, condition1, condition1_1, condition1_2, condition1_3, condition2, condition2_1, condition2_2, condition2_3;
        f1 = z3.mk_numeral("567", intT);
        f = z3.mk_numeral("0", intT);
        f2 = z3.mk_numeral("544", intT);
        condition1_1 = z3.mk_eq(func[0], args[0]);
        condition1_2 = z3.mk_eq(func[1], args[1]);
        condition1_3 = z3.mk_eq(func[2], args[2]);
        
        condition2_1 = z3.mk_eq(func[0], args2[0]);
        condition2_2 = z3.mk_eq(func[1], args2[1]);
        condition2_3 = z3.mk_eq(func[2], args2[2]);
        
        AstArray andArgs1 = new AstArrayTheirs(3);
        andArgs1.setitem(0, condition1_1);
        andArgs1.setitem(1, condition1_2);
        andArgs1.setitem(2, condition1_3);
        
        AstArray andArgs2 = new AstArrayTheirs(3);
        andArgs2.setitem(0, condition2_1);
        andArgs2.setitem(1, condition2_2);
        andArgs2.setitem(2, condition2_3);
        
        condition1 = z3.mk_and(3, andArgs1.cast());
        condition2 = z3.mk_and(3, andArgs2.cast());
        ite2 = z3.mk_ite(condition2, z3.mk_eq(fun, f2), z3.mk_eq(fun, f));
        ite1 = z3.mk_ite(condition1,z3.mk_eq(fun, f1) ,ite2);
        System.out.println("term : " + z3.ast_to_string(ite1));
        
        SWIGTYPE_p__Z3_ast z = z3.mk_var("z", intT);
        SWIGTYPE_p__Z3_ast[] addition = new SWIGTYPE_p__Z3_ast[2];
        addition[0] = z;
        addition[1] = func[1];
        SWIGTYPE_p__Z3_ast cntr1 = z3.mk_eq(fun, z3.mk_add(2, addition));
        SWIGTYPE_p__Z3_ast cntr2 = z3.mk_not(z3.mk_eq(fun, f));
        
        z3.assert_cnstr(ite1);
        z3.assert_cnstr(cntr2);
        z3.assert_cnstr(cntr1);
        SWIGTYPE_p__Z3_model model = z3.check_and_get_model_simple();
        
        if (model!=null) {
        	DisplayModel d = new DisplayModel();
        	d.display_model(model);
        } else {
        	System.out.println("unsat");
        }
        z3.pop(1);
	}
	

}
