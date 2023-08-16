<x-layout bodyClass="g-sidenav-show  bg-gray-200">
        <x-navbars.sidebar activePage="uploads"></x-navbars.sidebar>
        <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
            <!-- Navbar -->
            <x-navbars.navs.auth titlePage="Uploads"></x-navbars.navs.auth>
            <!-- End Navbar -->
            <div class="container-fluid px-2 px-md-4">
    <div class="card">
        <h4 class="card-header">UPLOAD DEPOSITS</h4>
        <div class="card-body">
            <h5>To view all available deposits, please input the file in the space below</h5>
        </div>
    </div>
    <br>
    <div class="card p-4">
        <form action="{{ route('upload') }}" method="POST" enctype="multipart/form-data">
            @csrf
            <div class="mb-3">
                <label for="csv_file" class="form-label">Upload CSV File</label>
                <input type="file" class="form-control" id="csv_file" name="csv_file">
            </div>
            <button type="submit" class="btn btn-primary">Upload</button>

            @if(session('success'))
                <div class="alert alert-success mt-3">
                    {{ session('success') }}
                </div>
            @endif

            @if($errors->any())
                <div class="alert alert-danger mt-3">
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
