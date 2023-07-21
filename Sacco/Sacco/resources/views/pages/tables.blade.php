<x-layout bodyClass="g-sidenav-show  bg-gray-200">
        <x-navbars.sidebar activePage="tables"></x-navbars.sidebar>
        <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
            <!-- Navbar -->
            <x-navbars.navs.auth titlePage="Tables"></x-navbars.navs.auth>
            <!-- End Navbar -->
            <div class="container-fluid px-2 px-md-4">
                <div class="card">
                    <h4>AVAILABLE DEPOSITS</h4>
                    <h5>To view all avilable deposits plase input the file in the space below</h5>
                </div>
                <br>
                <div class="card" padding="10pt">
                    
                <form action="{{ route('upload') }}" method="POST" enctype="multipart/form-data">
                  @csrf
                 <input type="file" name="csv_file">
                  <button type="submit">Upload</button>

                  @if(session('success'))
                     <div class="alert alert-success">
                        {{ session('success') }}
                    </div>
                    @endif
                    @if($errors->any())
                        <div class="alert alert-danger">
                         <ul>
                     @foreach($errors->all() as $error)
                     <li>{{ $error }}</li>
                         @endforeach
                         </ul>
                    </div>
                    @endif


                </form>

                </div>
            </div>   
        </main>
        <x-plugins></x-plugins>

</x-layout>
