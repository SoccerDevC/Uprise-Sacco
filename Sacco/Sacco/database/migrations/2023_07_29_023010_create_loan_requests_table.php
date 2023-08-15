<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateLoanRequestsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('loan_requests', function (Blueprint $table) {
            $table->integer('loan_application_number')->primary();
            $table->string('member_id');
            $table->decimal('loan_amount', 10, 2);
            $table->integer('payment_period');
<<<<<<< HEAD:Sacco/Sacco/database/migrations/2023_07_07_202042_create_loan_requests_table.php
            $table->integer('receipt_number')->nullable();
            $table->decimal('recommended_funds', 10, 2);
            $table->string('loan_approval_status');



=======
            $table->integer('recommended_funds');
            $table->string('loan_approval_status');
>>>>>>> ff9a022e33557a20e647628f9c06cb6ead585164:Sacco/Sacco/database/migrations/2023_07_29_023010_create_loan_requests_table.php
            
            
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('loan_requests');
    }
}