React.render(
		<div>
			<nav className="navbar navbar-inverse navbar-fixed-top">
		      <div className="container">
		        <div className="navbar-header">
		          <button type="button" className="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
		            <span className="sr-only">Toggle navigation</span>
		            <span className="icon-bar"></span>
		            <span className="icon-bar"></span>
		            <span className="icon-bar"></span>
		          </button>
		          <a className="navbar-brand" href="/">winitech</a>
		        </div>
		        <div id="navbar" className="collapse navbar-collapse">
		          <ul className="nav navbar-nav">
		            <li className="active"><a href="#">Home</a></li>
		            <li><a href="#about">About</a></li>
		            <li><a href="#contact">Contact</a></li>
		          </ul>
		          <ul className="nav navbar-nav navbar-right">
		            <li><a href="#">Sign up</a></li>
		            <li><a href="#about">Sign in</a></li>
		          </ul>
		        </div>{/* .nav-collapse */}
		      </div>
		    </nav>
		    
		    <div className="container">
	
		      <div className="starter-template">
		        <h1>Bootstrap starter template</h1>
		        <p className="lead">Use this document as a way to quickly start any new project.<br/> 
		        All you get is this text and a mostly barebones HTML document.
		        </p>
		      </div>
		    </div>{/*.container*/}
		 </div>
		, document.body);